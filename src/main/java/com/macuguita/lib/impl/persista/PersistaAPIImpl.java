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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;

import com.macuguita.lib.api.persista.DataToken;

public final class PersistaAPIImpl {

	private PersistaAPIImpl() {}

	public static <T> DataToken<T> register(Identifier id, Codec<T> codec) {
		return DataRegistry.add(id, codec);
	}

	public static CompletableFuture<Void> refreshAll(UUID playerId) {
		return DataCache.refresh(playerId, true);
	}
}
