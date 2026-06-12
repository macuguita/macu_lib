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
package com.macuguita.lib.impl.platform.neoforge;

import dev.yumi.commons.event.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;
import com.macuguita.lib.impl.platform.ClientAbstraction;

@ApiStatus.Internal
public record NeoClientAbstraction() implements ClientAbstraction {
	public static final NeoClientAbstraction INSTANCE = new NeoClientAbstraction();

	@Override
	public void registerPlayerJoinEvent(Event<Identifier, ClientPlayerJoinEvent> event) {
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, e -> {
			event.invoker().playerJoin(e.getPlayer().connection, Minecraft.getInstance());
		});
	}

	@Override
	public void registerPlayerLeaveEvent(Event<Identifier, ClientPlayerLeaveEvent> event) {
		NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingOut.class, e -> {
			if (e.getPlayer() == null) return;
			event.invoker().playerLeave(e.getPlayer().connection, Minecraft.getInstance());
		});
	}

	@Override
	public <T extends CustomPacketPayload> void registerGlobalReceiverPlay(
		CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver
	) {
		NeoCommonAbstraction.INSTANCE.addLateAction(bus -> {
			bus.addListener(RegisterClientPayloadHandlersEvent.class, e -> {
				e.register(
					type,
					(p, ctx) ->
						receiver.receive(Minecraft.getInstance(), (LocalPlayer) ctx.player(), p));
			});
		});
	}
}
