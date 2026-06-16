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

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;

// In-memory cache of player data, keyed by player UUID and data identifier
@ApiStatus.Internal
final class DataCache {

	private static final Map<UUID, Map<ResourceLocation, CachedValue<?>>> STORE = new HashMap<>();

	private DataCache() {}

	@SuppressWarnings("unchecked")
	static <T> CachedValue<T> getOrCreate(UUID playerId, DataEntry<T> entry) {
		synchronized (STORE) {
			return (CachedValue<T>) STORE
				.computeIfAbsent(playerId, k -> new HashMap<>())
				.computeIfAbsent(entry.id(), id -> CachedValue.load(entry, playerId));
		}
	}

	@SuppressWarnings("unchecked")
	static <T> CachedValue<T> getOrEmpty(UUID playerId, DataEntry<T> entry) {
		synchronized (STORE) {
			return (CachedValue<T>) STORE
				.computeIfAbsent(playerId, k -> new HashMap<>())
				.computeIfAbsent(entry.id(), id -> CachedValue.empty(entry, playerId));
		}
	}

	static <T> CachedValue<T> lookup(UUID playerId, DataEntry<T> entry, boolean forceRefresh) {
		var value = getOrCreate(playerId, entry);
		if (forceRefresh) {
			value.reload();
		}
		return value;
	}

	static <T> Optional<T> getCached(UUID playerId, DataEntry<T> entry) {
		return getOrEmpty(playerId, entry).value();
	}

	static CompletableFuture<Void> refresh(UUID playerId, boolean force) {
		var startTime = Instant.now();
		return CompletableFuture.allOf(
			DataRegistry.values().parallelStream()
				.map(entry -> lookup(playerId, entry, force).asFuture())
				.toArray(CompletableFuture[]::new)
		).thenRun(() -> {
			var duration = Duration.between(startTime, Instant.now());
			Persista.LOGGER.info("Loaded {} data entries for player {} (took {}s {}ms)",
				DataRegistry.size(), playerId, duration.toSeconds(), duration.toMillisPart());
		});
	}

	// might expose this to the api, not sure
	static void evict(UUID playerId) {
		synchronized (STORE) {
			STORE.remove(playerId);
		}
	}
}
