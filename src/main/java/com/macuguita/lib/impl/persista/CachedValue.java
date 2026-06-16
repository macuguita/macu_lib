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

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import com.macuguita.lib.api.persista.PersistaAPI;

// Holds cached value for specific player and entry
@ApiStatus.Internal
final class CachedValue<T> {

	private final DataEntry<T> entry;
	private final UUID playerId;

	private @Nullable T value;
	private Instant expiresAt;
	private @Nullable CompletableFuture<Void> pendingFetch;

	private CachedValue(DataEntry<T> entry, UUID playerId, @Nullable T value) {
		this.entry = entry;
		this.playerId = playerId;
		this.value = value;
		this.expiresAt = Instant.MIN; // expired by default so first access triggers a fetch
	}

	static <T> CachedValue<T> load(DataEntry<T> entry, UUID playerId) {
		var holder = new CachedValue<>(entry, playerId, null);
		holder.reload();
		return holder;
	}

	static <T> CachedValue<T> empty(DataEntry<T> entry, UUID playerId) {
		return new CachedValue<>(entry, playerId, null);
	}

	Optional<T> value() {
		if (isExpired() && pendingFetch == null) {
			reload(); // only trigger once
		}
		return Optional.ofNullable(value);
	}

	void setValue(@Nullable T value) {
		this.value = value;
		this.expiresAt = Instant.now().plus(PersistaAPI.CACHE_DURATION);
	}

	T or(T defaultValue) {
		var v = value();
		return v.orElse(defaultValue);
	}

	boolean isExpired() {
		return Instant.now().isAfter(expiresAt);
	}

	synchronized void reload() {
		if (pendingFetch != null && !pendingFetch.isDone()) {
			pendingFetch.cancel(true);
		}

		var selfRef = new MutableObject<CompletableFuture<Void>>();
		var future = CompletableFuture.supplyAsync(() ->
			entry.fetchRemote(playerId)
		).thenAccept(oFetched -> {
			synchronized (this) {
				if (pendingFetch != selfRef.get()) return;
				oFetched.ifPresentOrElse(
					this::setValue,
					() -> expiresAt = Instant.now().plusSeconds(30)
				);
				pendingFetch = null;
			}
		}).exceptionally(t -> {
			Persista.LOGGER.error("Failed to fetch {} for player {}", entry.id(), playerId, t);
			return null;
		});

		selfRef.setValue(future);
		pendingFetch = future;
	}

	CompletableFuture<Optional<T>> asFuture() {
		if (pendingFetch != null && !pendingFetch.isDone()) {
			return pendingFetch.thenApply(v -> Optional.ofNullable(value));
		}
		return CompletableFuture.completedFuture(Optional.ofNullable(value));
	}
}
