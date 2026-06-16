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

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.macuguita.lib.impl.persista.PersistaAPIImpl;
import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;

/**
 * Entry point for registering and interacting with Persista data tokens.
 *
 * <h2>Overview</h2>
 * Persista provides cached, network-backed persistent player data.
 * Data may be fetched from or written to a remote HTTP API depending on cache state.
 *
 * <h2>Important behavior guarantees</h2>
 * <ul>
 *   <li>Operations may trigger asynchronous network I/O.</li>
 *   <li>Cache misses and stale values may result in HTTP requests.</li>
 *   <li>Calls are non-blocking unless explicitly stated otherwise.</li>
 * </ul>
 *
 * <h2>Rate limiting & server behavior</h2>
 * <ul>
 *   <li>The Persista API server enforces rate limiting and may respond with HTTP 429.</li>
 *   <li>Clients MUST handle rate limiting responses and respect retry behavior.</li>
 *   <li>This library does NOT impose additional client-side rate limiting.</li>
 * </ul>
 *
 * <h2>Usage guidelines</h2>
 * <ul>
 *   <li>Do not call data access methods in tight loops or per-tick logic unless cached.</li>
 *   <li>Prefer caching results locally when accessed frequently.</li>
 *   <li>Avoid repeated writes in rapid succession (debounce updates).</li>
 * </ul>
 */
public final class PersistaAPI {

	public static final String API_URL = "https://persista.macuguita.com/api";
	public static final Duration CACHE_DURATION = Duration.ofHours(1);

	private PersistaAPI() {}

	/**
	 * Registers a new data token for the given identifier and codec.
	 *
	 * <p>This should be called during mod initialization only.</p>
	 *
	 * @param id    unique identifier for the data type
	 * @param codec serialization codec for the data type
	 * @param <T>   type of stored data
	 * @return a DataToken representing the registered data
	 */
	public static <T> DataToken<T> register(ResourceLocation id, Codec<T> codec) {
		return PersistaAPIImpl.register(id, codec);
	}

	/**
	 * Forces a refresh of all registered data for the given player.
	 *
	 * <p>This will trigger multiple network requests in parallel or sequence
	 * depending on internal implementation.</p>
	 *
	 * <p>Callers should avoid invoking this frequently as it may cause
	 * significant network load.</p>
	 *
	 * @param playerId target player UUID
	 * @return future that completes when all refresh operations finish
	 */
	public static CompletableFuture<Void> refreshAll(UUID playerId) {
		return PersistaAPIImpl.refreshAll(playerId);
	}
}
