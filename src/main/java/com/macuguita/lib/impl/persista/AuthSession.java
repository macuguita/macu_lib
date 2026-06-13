/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.persista;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.UUID;

import com.google.gson.JsonParser;
import com.macuguita.lib.api.persista.PersistaAPI;
import com.mojang.authlib.exceptions.AuthenticationException;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;

// Manages the mojang auth and stores JWT session
final class AuthSession {

	private static final HttpClient HTTP = HttpClient.newHttpClient();

	@Nullable
	private static Session current;

	private AuthSession() {}

	record Session(String accessToken, Instant expiresAt) {
		boolean isValid() {
			return Instant.now().isBefore(expiresAt.minusSeconds(60));
		}
	}

	@Nullable
	static Session getOrLogin() {
		if (current != null && current.isValid()) {
			return current;
		}
		return login();
	}

	@Nullable
	static UUID getClientPlayerId() {
		var profile = Minecraft.getInstance().getGameProfile();
		return profile./*? if >= 1.21.11 {*/id/*?} else {*//*getId*//*?}*/();
	}

	static boolean isOffline() {
		var profile = Minecraft.getInstance().getGameProfile();
		return UUIDUtil.createOfflinePlayerUUID(profile./*? if >= 1.21.11 {*/name/*?} else {*//*getName*//*?}*/()).equals(profile./*? if >= 1.21.11 {*/id/*?} else {*//*getId*//*?}*/());
	}

	@Nullable
	private static Session login() {
		try {
			var playerId = getClientPlayerId();
			if (playerId == null) return null;

			var challenge = fetchChallenge(playerId);
			if (challenge == null) return null;

			var username = Minecraft.getInstance().getGameProfile()./*? if >= 1.21.11 {*/name/*?} else {*//*getName*//*?}*/();
			joinServer(challenge);

			current = verify(playerId, username, challenge);
			return current;
		} catch (Exception e) {
			PersistaLogger.get().error("Persista login failed", e);
			return null;
		}
	}

	@Nullable
	private static String fetchChallenge(UUID playerId) throws Exception {
		var uri = URI.create(PersistaAPI.API_URL + "/auth/mojang/challenge");
		var body = "{\"id\":\"" + playerId + "\"}";
		var request = HttpRequest.newBuilder(uri)
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.header("Content-Type", "application/json")
				.build();
		var response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			PersistaLogger.get().error("Challenge request failed with status {}", response.statusCode());
			return null;
		}
		var json = JsonParser.parseString(response.body()).getAsJsonObject();
		return json.get("token").getAsString();
	}

	private static void joinServer(String challenge) {
		var mc = Minecraft.getInstance();
		var profile = mc.getGameProfile();
		try {
			mc./*? if >= 1.21.11 {*/services/*?} else {*//*getMinecraftSessionService*//*?}*/()./*? if >= 1.21.11 {*/sessionService()./*?}*/joinServer(profile./*? if >= 1.21.11 {*/id/*?} else {*//*getId*//*?}*/(), mc.getUser().getAccessToken(), challenge);
		} catch (AuthenticationException e) {
			throw new RuntimeException("Mojang authentication failed", e);
		}
	}

	@Nullable
	private static Session verify(UUID playerId, String username, String challenge) throws Exception {
		var uri = URI.create(PersistaAPI.API_URL + "/auth/mojang");
		var body = String.format("{\"id\":\"%s\",\"username\":\"%s\",\"token\":\"%s\"}",
				playerId, username, challenge);
		var request = HttpRequest.newBuilder(uri)
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.header("Content-Type", "application/json")
				.build();
		var response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			PersistaLogger.get().error("Persista verify failed with status {}", response.statusCode());
			return null;
		}
		var json = JsonParser.parseString(response.body()).getAsJsonObject();
		var token = json.get("session_token").getAsString();
		var expiresAt = Instant.parse(json.get("expires_at").getAsString());
		return new Session(token, expiresAt);
	}
}
