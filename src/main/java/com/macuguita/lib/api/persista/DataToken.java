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
 * Instances are created via {@link PersistaAPI#register}.
 *
 * <h2>Behavior model</h2>
 * Data is backed by a remote HTTP service and a local cache.
 * Depending on cache state, operations may trigger network requests.
 *
 * <h2>Important guarantees</h2>
 * <ul>
 *   <li>Methods are non-blocking unless explicitly stated.</li>
 *   <li>Cache access may trigger asynchronous fetches.</li>
 *   <li>Network requests are not guaranteed to be immediate or deduplicated.</li>
 * </ul>
 *
 * <h2>Rate limiting</h2>
 * <ul>
 *   <li>The Persista server enforces rate limiting and may return HTTP 429.</li>
 *   <li>This library does NOT perform client-side rate limiting.</li>
 *   <li>Callers must avoid calling methods in hot loops or per-tick updates.</li>
 * </ul>
 *
 * <h2>Threading</h2>
 * Implementations may perform asynchronous network operations.
 * Callers should not assume thread safety beyond documented cache behavior.
 *
 * @param <T> type of stored data
 */
public interface DataToken<T> {

	/**
	 * Fetches the data for the given player from the remote server.
	 *
	 * <p>This method may perform an HTTP request if no valid cached value exists.</p>
	 *
	 * <p>This operation is asynchronous and may be subject to server rate limiting (HTTP 429).</p>
	 */
	CompletableFuture<Optional<T>> fetch(UUID playerId);

	/**
	 * Returns cached data for the given player.
	 *
	 * <p>If the cached value is stale or missing, this method may trigger
	 * an asynchronous background fetch.</p>
	 *
	 * <p>Repeated calls may therefore indirectly result in network traffic.</p>
	 */
	Optional<T> get(UUID playerId);

	/**
	 * Returns cached data for the given player, or a default value if none exists.
	 *
	 * <p>This method may still trigger background refresh behavior depending on cache state.</p>
	 */
	T getOrDefault(UUID playerId, T defaultValue);

	/**
	 * Returns cached data without triggering any network activity.
	 *
	 * <p>This method is fully local and safe to call in performance-critical loops.</p>
	 */
	Optional<T> getCached(UUID playerId);

	/**
	 * Writes new data for the local player to the remote server.
	 *
	 * <p>This operation performs asynchronous network communication and may be
	 * rejected by the server with HTTP 429 (rate limited).</p>
	 *
	 * <p>Callers MUST avoid spamming updates. The library does not throttle writes.</p>
	 *
	 * @throws UnsupportedOperationException if called on the server side
	 */
	CompletableFuture<Void> setData(T data);
}
