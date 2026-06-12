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

import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;

import com.macuguita.lib.impl.persista.PersistaAPIImpl;

/**
 * Entry point for registering and interacting with Persista data tokens.
 */
public final class PersistaAPI {

	public static final String API_URL = "https://persista.macuguita.com/api";
	public static final Duration CACHE_DURATION = Duration.ofHours(1);

	private PersistaAPI() {}

	/**
	 * Registers a new data token for the given identifier and codec.
	 * Call this during mod initialization.
	 */
	public static <T> DataToken<T> register(Identifier id, Codec<T> codec) {
		return PersistaAPIImpl.register(id, codec);
	}

	/**
	 * Forces a refresh of all registered data for the given player.
	 */
	public static CompletableFuture<Void> refreshAll(UUID playerId) {
		return PersistaAPIImpl.refreshAll(playerId);
	}
}
