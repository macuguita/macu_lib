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
package com.macuguita.lib.api.persista;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A handle to a piece of persistent player data stored in Persista.
 * Register instances via {@link PersistaAPI#register(net.minecraft.resources.Identifier, com.mojang.serialization.Codec)}.
 *
 * @param <T> the type of data this token represents
 */
public interface DataToken<T> {

	/**
	 * Fetches the data for the given player from the remote server.
	 * Returns a future that completes once the value has been retrieved.
	 */
	CompletableFuture<Optional<T>> fetch(UUID playerId);

	/**
	 * Returns the cached value for the given player immediately without blocking.
	 * Triggers a background fetch if the cache is stale.
	 */
	Optional<T> get(UUID playerId);

	/**
	 * Returns the cached value, or {@code defaultValue} if not present.
	 */
	T getOrDefault(UUID playerId, T defaultValue);

	/**
	 * Returns the cached value without triggering any network activity.
	 */
	Optional<T> getCached(UUID playerId);

	/**
	 * Writes new data for the local player to the remote server.
	 * Must only be called from the client side.
	 *
	 * @throws UnsupportedOperationException if called on the server
	 */
	CompletableFuture<Void> setData(T data);
}
