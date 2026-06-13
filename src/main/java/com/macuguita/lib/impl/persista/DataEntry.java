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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import com.macuguita.lib.api.persista.DataToken;
import com.macuguita.lib.api.persista.PersistaAPI;
import com.macuguita.lib.impl.platform.CommonAbstraction;

// Internal impl of DataToken handles fetching and stuff
@ApiStatus.Internal
record DataEntry<T>(Identifier id, Codec<T> codec) implements DataToken<T> {

	private static final HttpClient HTTP = HttpClient.newHttpClient();

	@Override
	public CompletableFuture<Optional<T>> fetch(UUID playerId) {
		return DataCache.lookup(playerId, this, false).asFuture();
	}

	@Override
	public Optional<T> get(UUID playerId) {
		return Optional.ofNullable(DataCache.lookup(playerId, this, false).value());
	}

	@Override
	public T getOrDefault(UUID playerId, T defaultValue) {
		return DataCache.lookup(playerId, this, false).or(defaultValue);
	}

	@Override
	public Optional<T> getCached(UUID playerId) {
		return DataCache.getCached(playerId, this);
	}

	@Override
	public CompletableFuture<Void> setData(T data) {
		if (!isClient()) {
			throw new UnsupportedOperationException("setData() must only be called on the client");
		}

		var playerId = AuthSession.getClientPlayerId();
		if (playerId == null) {
			return CompletableFuture.failedFuture(new IllegalStateException("No local player found"));
		}

		// update cache immediately so the client sees the change right away
		var cached = DataCache.getOrEmpty(playerId, this);
		var previous = cached.value();
		cached.setValue(data);

		if (AuthSession.isOffline()) {
			PersistaLogger.get().debug("Client is in offline mode, cannot persist data for {}", id);
			return CompletableFuture.completedFuture(null);
		}

		// encode early on the calling thread so data doesn't need to be thread-safe
		var json = codec.encodeStart(JsonOps.INSTANCE, data)
			.resultOrPartial(err -> PersistaLogger.get().error("Failed to encode {} : {}", id, err))
			.orElseThrow();

		return CompletableFuture.runAsync(() -> {
			var session = AuthSession.getOrLogin();
			if (session == null) {
				throw new RuntimeException("Failed to authenticate with Persista");
			}
			postRemote(playerId, json, session.accessToken());
			C2SDataUpdatedPacket.trySend(id());
		}).exceptionally(t -> {
			PersistaLogger.get().error("Failed to write {} for player {}, reverting", id, playerId, t);
			cached.setValue(previous);
			return null;
		});
	}

	@Nullable
	T fetchRemote(UUID playerId) {
		var uri = dataUri(playerId);
		try {
			var request = HttpRequest.newBuilder(uri).GET().build();
			var response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
			if (response.statusCode() == 404) {
				return null;
			}
			if (response.statusCode() != 200) {
				PersistaLogger.get().warn("Unexpected status {} fetching {} for {}", response.statusCode(), id, playerId);
				return null;
			}
			var json = JsonParser.parseString(response.body());
			return codec.decode(JsonOps.INSTANCE, json)
				.resultOrPartial(err -> PersistaLogger.get().error("Failed to decode {} for {}: {}", id, playerId, err))
				.map(pair -> pair.getFirst())
				.orElse(null);
		} catch (IOException | InterruptedException e) {
			PersistaLogger.get().error("Network error fetching {} for {}", id, playerId, e);
			return null;
		}
	}

	private void postRemote(UUID playerId, JsonElement json, String accessToken) {
		var uri = dataUri(playerId);
		try {
			var body = json.toString();
			var request = HttpRequest.newBuilder(uri)
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.header("Content-Type", "application/json")
				.header("Authorization", "Bearer " + accessToken)
				.build();
			var response = HTTP.send(request, HttpResponse.BodyHandlers.discarding());
			if (response.statusCode() != 204) {
				PersistaLogger.get().warn("Unexpected status {} writing {} for {}", response.statusCode(), id, playerId);
			}
		} catch (IOException | InterruptedException e) {
			PersistaLogger.get().error("Network error writing {} for {}", id, playerId, e);
			throw new RuntimeException(e);
		}
	}

	private URI dataUri(UUID playerId) {
		return URI.create(String.format("%s/v0/data/%s/%s/%s",
			PersistaAPI.API_URL, playerId, id.getNamespace(), id.getPath()));
	}

	private static boolean isClient() {
		return CommonAbstraction.get().isClient();
	}
}
