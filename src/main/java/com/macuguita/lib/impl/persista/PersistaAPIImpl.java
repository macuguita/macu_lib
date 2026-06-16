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

import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.impl.MacuLib;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import com.mojang.serialization.Codec;

import com.macuguita.lib.api.persista.DataToken;

@ApiStatus.Internal
public class PersistaAPIImpl {

	public static <T> DataToken<T> register(Identifier id, Codec<T> codec) {
		return DataRegistry.add(id, codec);
	}

	public static CompletableFuture<Void> refreshAll(UUID playerId) {
		if (!MacuLib.CONFIG.persista.loginAutofetch) {
			return CompletableFuture.completedFuture(null);
		}
		return DataCache.refresh(playerId, MacuLib.CONFIG.persista.loginForceRefresh);
	}
}
