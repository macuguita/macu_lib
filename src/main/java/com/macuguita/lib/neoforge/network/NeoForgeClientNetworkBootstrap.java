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

//? neoforge && >= 1.21.11 {

/*import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientNetworkBootstrap {

	public static final List<ClientHandlerRegistration<?>> CLIENT_HANDLERS = new ArrayList<>();

	@SubscribeEvent
	public static void registerClientHandlers(RegisterClientPayloadHandlersEvent event) {
		for (NetworkManager.S2CRegistration<?> reg : NeoForgeNetworkBootstrap.S2C) {
			registerS2CHandler(event, reg);
		}
		for (ClientHandlerRegistration<?> reg : CLIENT_HANDLERS) {
			registerS2CHandler(event, reg);
		}
	}

	private static <T extends CustomPacketPayload> void registerS2CHandler(
			RegisterClientPayloadHandlersEvent event,
			NetworkManager.S2CRegistration<T> reg
	) {
		if (reg.handlerSupplier() == null) return;
		event.register(reg.type(), (payload, context) -> {
			// Handler supplier is called on client side only
			var handler = reg.handlerSupplier().get();
			handler.accept(payload);
		});
	}

	private static <T extends CustomPacketPayload> void registerS2CHandler(
			RegisterClientPayloadHandlersEvent event,
			ClientHandlerRegistration<T> reg
	) {
		event.register(
				reg.type(),
				(payload, context) -> reg.handler().accept(payload)
		);
	}

	public record ClientHandlerRegistration<T extends CustomPacketPayload>(
			CustomPacketPayload.Type<T> type,
			Consumer<T> handler
	) {}
}
*///?}
