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
package com.macuguita.lib.impl.platform.fabric;

import dev.yumi.commons.event.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;
import com.macuguita.lib.impl.platform.ClientAbstraction;

@ApiStatus.Internal
public record FabricClientAbstraction() implements ClientAbstraction {
	public static final FabricClientAbstraction INSTANCE = new FabricClientAbstraction();

	@Override
	public void registerPlayerJoinEvent(Event<Identifier, ClientPlayerJoinEvent> event) {
		ClientPlayConnectionEvents.JOIN.register(
			(listener, _, client) -> event.invoker().playerJoin(listener, client));
	}

	@Override
	public void registerPlayerLeaveEvent(Event<Identifier, ClientPlayerLeaveEvent> event) {
		ClientPlayConnectionEvents.DISCONNECT.register(
			(listener, client) -> event.invoker().playerLeave(listener, client));
	}

	@Override
	public <T extends CustomPacketPayload> void registerGlobalReceiverPlay(
		CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver
	) {
		ClientPlayNetworking.registerGlobalReceiver(
			type, (p, ctx) -> receiver.receive(ctx.client(), ctx.player(), p));
	}
}
