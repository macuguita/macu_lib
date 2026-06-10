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
package com.macuguita.lib.impl.platform;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiMods;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Util;

import com.macuguita.lib.api.event.lifecyle.ServerStartedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartingEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppingEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerLeaveEvent;
import com.macuguita.lib.api.reg.GuitaRegistry;

@ApiStatus.Internal
public interface CommonAbstraction {
	boolean IS_FABRIC =
		YumiMods.get().isModLoaded("fabricloader") && !YumiMods.get().isModLoaded("connector");

	CommonAbstraction INSTANCE =
		Util.make(
			() -> {
				try {
					return (CommonAbstraction)
						Class.forName(
								"com.macuguita.lib.impl.platform."
									+ (CommonAbstraction.IS_FABRIC
									? "fabric.FabricCommonAbstraction"
									: "neoforge.NeoCommonAbstraction"))
							.getField("INSTANCE")
							.get(null);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			});

	static CommonAbstraction get() {
		return INSTANCE;
	}

	// Events
	// TODO: This is definitely not a good way of registering them.
	//  I can't think of anything better
	void registerServerStartingEvent(Event<Identifier, ServerStartingEvent> event);

	void registerServerStartedEvent(Event<Identifier, ServerStartedEvent> event);

	void registerServerStoppingEvent(Event<Identifier, ServerStoppingEvent> event);

	void registerServerStoppedEvent(Event<Identifier, ServerStoppedEvent> event);

	void registerPlayerJoinEvent(Event<Identifier, ServerPlayerJoinEvent> event);

	void registerPlayerLeaveEvent(Event<Identifier, ServerPlayerLeaveEvent> event);

	<T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id);

	<T extends CustomPacketPayload> void registerServerboundPlayPayload(
		CustomPacketPayload.Type<T> type,
		StreamCodec<RegistryFriendlyByteBuf, T> codec,
		PlayPacketReceiver<T> receiver);

	<T extends CustomPacketPayload> void registerClientboundPlayPayload(
		CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec);

	interface PlayPacketReceiver<T> {
		void receive(ServerPlayer player, T payload);
	}
}
