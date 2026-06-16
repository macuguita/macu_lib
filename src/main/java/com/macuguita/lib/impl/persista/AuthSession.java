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
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.JsonParser;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;

import com.mojang.authlib.exceptions.AuthenticationException;

import com.macuguita.lib.api.persista.PersistaAPI;

// Manages the mojang auth and stores JWT session
@ApiStatus.Internal
final class AuthSession {

	private static Instant loginCooldownUntil = Instant.MIN;

	private static @Nullable Session current;

	private AuthSession() {}

	record Session(String accessToken, Instant expiresAt) {
		boolean isValid() {
			return Instant.now().isBefore(expiresAt.minusSeconds(60));
		}
	}

	static Optional<Session> getOrLogin() {
		if (current != null && current.isValid()) {
			return Optional.of(current);
		}
		if (Instant.now().isBefore(loginCooldownUntil)) {
			Persista.LOGGER.debug("Auth on cooldown, skipping login attempt");
			return Optional.empty();
		}
		return login();
	}

	static Optional<UUID> getClientPlayerId() {
		var profile = Minecraft.getInstance().getGameProfile();
		return Optional.of(profile.id());
	}

	static boolean isOffline() {
		var profile = Minecraft.getInstance().getGameProfile();
		return UUIDUtil.createOfflinePlayerUUID(profile.name()).equals(profile.id());
	}

	private static Optional<Session> login() {
		try {
			var playerId = getClientPlayerId();
			if (playerId.isEmpty()) return Optional.empty();

			var challenge = fetchChallenge(playerId.get());
			if (challenge.isEmpty()) return Optional.empty();

			var username = Minecraft.getInstance().getGameProfile().name();
			joinServer(challenge.get());

			current = verify(playerId.get(), username, challenge.get()).orElse(null);
			return Optional.ofNullable(current);
		} catch (Exception e) {
			Persista.LOGGER.error("Persista login failed", e);
			loginCooldownUntil = Instant.now().plusSeconds(60);
			return Optional.empty();
		}
	}

	@SuppressWarnings("resource")
	private static Optional<String> fetchChallenge(UUID playerId) throws Exception {
		var uri = URI.create(PersistaAPI.API_URL + "/auth/mojang/challenge");
		var body = "{\"id\":\"" + playerId + "\"}";
		var request = HttpHelper.post(uri, body).build();
		var response = HttpHelper.client().send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			Persista.LOGGER.error("Challenge request failed with status {}", response.statusCode());
			return Optional.empty();
		}
		var json = JsonParser.parseString(response.body()).getAsJsonObject();
		return Optional.ofNullable(json.get("token").getAsString());
	}

	private static void joinServer(String challenge) {
		var mc = Minecraft.getInstance();
		var profile = mc.getGameProfile();
		try {
			mc.services().sessionService().joinServer(profile.id(), mc.getUser().getAccessToken(), challenge);
		} catch (AuthenticationException e) {
			throw new RuntimeException("Mojang authentication failed", e);
		}
	}

	@SuppressWarnings("resource")
	private static Optional<Session> verify(UUID playerId, String username, String challenge) throws Exception {
		var uri = URI.create(PersistaAPI.API_URL + "/auth/mojang");
		var body = String.format("{\"id\":\"%s\",\"username\":\"%s\",\"token\":\"%s\"}",
			playerId, username, challenge);
		var request = HttpHelper.post(uri, body).build();
		var response = HttpHelper.client().send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			Persista.LOGGER.error("Persista verify failed with status {}", response.statusCode());
			return Optional.empty();
		}
		var json = JsonParser.parseString(response.body()).getAsJsonObject();
		var token = json.get("session_token").getAsString();
		var expiresAt = Instant.parse(json.get("expires_at").getAsString());
		return Optional.of(new Session(token, expiresAt));
	}
}
