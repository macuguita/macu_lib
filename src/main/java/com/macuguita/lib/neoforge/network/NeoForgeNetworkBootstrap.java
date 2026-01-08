/*
 * macu_lib
 * Copyright (C) 2026 macuguita
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

package com.macuguita.lib.neoforge.network;

//? neoforge {

/*import java.util.ArrayList;
import java.util.List;

import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID)
public final class NeoForgeNetworkBootstrap {

	public static final List<NetworkManager.C2SRegistration<?>> C2S = new ArrayList<>();
	public static final List<NetworkManager.S2CRegistration<?>> S2C = new ArrayList<>();

	@SubscribeEvent
	public static void register(RegisterPayloadHandlersEvent event) {
		for (var reg : C2S) {
			registerC2S(event, reg);
		}
		for (var reg : S2C) {
			registerS2C(event, reg);
		}
	}

	private static <T extends CustomPacketPayload> void registerC2S(
			RegisterPayloadHandlersEvent event,
			NetworkManager.C2SRegistration<T> reg
	) {
		event.registrar(reg.type().id().getNamespace())
				.optional()
				.playToServer(
						reg.type(),
						reg.codec(),
						(payload, ctx) -> {
							var player = ctx.player();
							if (player instanceof ServerPlayer serverPlayer) {
								var handler = reg.handlerSupplier().get();
								handler.accept(payload, serverPlayer);
							}
						}
				);
	}

	private static <T extends CustomPacketPayload> void registerS2C(
			RegisterPayloadHandlersEvent event,
			NetworkManager.S2CRegistration<T> reg
	) {
		event.registrar(reg.type().id().getNamespace())
				.optional()
				.playToClient(
						reg.type(),
						reg.codec()
						//? < 1.21.11 {
						/^, (payload, ctx) -> {
							var handler = reg.handlerSupplier().get();
							handler.accept(payload);
						}
						^///?}
				);
	}
}
*///?}
