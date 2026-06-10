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

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import com.macuguita.lib.api.event.lifecyle.ServerStartedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartingEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppingEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerLeaveEvent;
import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.impl.platform.CommonAbstraction;
import com.macuguita.lib.impl.platform.fabric.reg.FabricGuitaRegistry;

public record FabricCommonAbstraction() implements CommonAbstraction {
	public static final FabricCommonAbstraction INSTANCE = new FabricCommonAbstraction();

	@Override
	public void registerServerStartingEvent(Event<Identifier, ServerStartingEvent> event) {
		ServerLifecycleEvents.SERVER_STARTING.register(
			mcServer -> event.invoker().serverStarting(mcServer));
	}

	@Override
	public void registerServerStartedEvent(Event<Identifier, ServerStartedEvent> event) {
		ServerLifecycleEvents.SERVER_STARTED.register(
			mcServer -> event.invoker().serverStarted(mcServer));
	}

	@Override
	public void registerServerStoppingEvent(Event<Identifier, ServerStoppingEvent> event) {
		ServerLifecycleEvents.SERVER_STOPPING.register(
			mcServer -> event.invoker().serverStopping(mcServer));
	}

	@Override
	public void registerServerStoppedEvent(Event<Identifier, ServerStoppedEvent> event) {
		ServerLifecycleEvents.SERVER_STOPPED.register(
			mcServer -> event.invoker().serverStopped(mcServer));
	}

	@Override
	public void registerPlayerJoinEvent(Event<Identifier, ServerPlayerJoinEvent> event) {
		ServerPlayerEvents.JOIN.register(
			player -> event.invoker().playerJoin(player));
	}

	@Override
	public void registerPlayerLeaveEvent(Event<Identifier, ServerPlayerLeaveEvent> event) {
		ServerPlayerEvents.LEAVE.register(
			player -> event.invoker().playerLeave(player));
	}

	@Override
	public <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id) {
		return new FabricGuitaRegistry<>(registry, id);
	}

	@Override
	public <T extends CustomPacketPayload> void registerServerboundPlayPayload(
		CustomPacketPayload.Type<T> type,
		StreamCodec<RegistryFriendlyByteBuf, T> codec,
		PlayPacketReceiver<T> receiver
	) {
		PayloadTypeRegistry.serverboundPlay().register(type, codec);
		ServerPlayNetworking.registerGlobalReceiver(
			type, (payload, context) -> receiver.receive(context.player(), payload));
	}

	@Override
	public <T extends CustomPacketPayload> void registerClientboundPlayPayload(
		CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec
	) {
		PayloadTypeRegistry.clientboundPlay().register(type, codec);
	}
}
