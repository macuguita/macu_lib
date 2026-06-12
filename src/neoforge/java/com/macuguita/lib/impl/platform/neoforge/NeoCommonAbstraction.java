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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dev.yumi.commons.event.Event;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartingEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppingEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerLeaveEvent;
import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.impl.creativetab.ModifyCreativeTabOutputEvents;
import com.macuguita.lib.impl.platform.CommonAbstraction;
import com.macuguita.lib.impl.platform.neoforge.creativetab.NeoForgeGuitaCreativeModeTabOutput;
import com.macuguita.lib.impl.platform.neoforge.reg.NeoForgeGuitaRegistry;

@ApiStatus.Internal
public record NeoCommonAbstraction(List<Consumer<IEventBus>> lateActions) implements CommonAbstraction {
	public static @Nullable IEventBus EVENT_BUS = null;
	public static final NeoCommonAbstraction INSTANCE = new NeoCommonAbstraction(new ArrayList<>());

	@Override
	public void registerServerStartingEvent(Event<Identifier, ServerStartingEvent> event) {
		NeoForge.EVENT_BUS.addListener(net.neoforged.neoforge.event.server.ServerStartingEvent.class, e -> {
			event.invoker().serverStarting(e.getServer());
		});
	}

	@Override
	public void registerServerStartedEvent(Event<Identifier, ServerStartedEvent> event) {
		NeoForge.EVENT_BUS.addListener(net.neoforged.neoforge.event.server.ServerStartedEvent.class, e -> {
			event.invoker().serverStarted(e.getServer());
		});
	}

	@Override
	public void registerServerStoppingEvent(Event<Identifier, ServerStoppingEvent> event) {
		NeoForge.EVENT_BUS.addListener(net.neoforged.neoforge.event.server.ServerStoppingEvent.class, e -> {
			event.invoker().serverStopping(e.getServer());
		});
	}

	@Override
	public void registerServerStoppedEvent(Event<Identifier, ServerStoppedEvent> event) {
		NeoForge.EVENT_BUS.addListener(net.neoforged.neoforge.event.server.ServerStoppedEvent.class, e -> {
			event.invoker().serverStopped(e.getServer());
		});
	}

	@Override
	public void registerPlayerJoinEvent(Event<Identifier, ServerPlayerJoinEvent> event) {
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedInEvent.class, e -> {
			event.invoker().playerJoin((ServerPlayer) e.getEntity());
		});
	}

	@Override
	public void registerPlayerLeaveEvent(Event<Identifier, ServerPlayerLeaveEvent> event) {
		NeoForge.EVENT_BUS.addListener(PlayerEvent.PlayerLoggedOutEvent.class, e -> {
			event.invoker().playerLeave((ServerPlayer) e.getEntity());
		});
	}

	@Override
	public void registerModifyCreativeTabOutputEvent(Event<Identifier, ModifyCreativeTabOutputEvent> event) {
		addLateAction(bus ->
			bus.addListener(BuildCreativeModeTabContentsEvent.class, e -> {
				var wrapped = new NeoForgeGuitaCreativeModeTabOutput(e);
				event.invoker().modifyOutput(e.getTab(), wrapped);

				// also fire the per-tab event if registered
				var perTab = ModifyCreativeTabOutputEvents.get(e.getTabKey());
				if (perTab != null) perTab.invoker().modifyOutput(e.getTab(), wrapped);
			}));
	}

	@Override
	public <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id) {
		return new NeoForgeGuitaRegistry<>(registry, id);
	}

	@Override
	public <T extends CustomPacketPayload> void registerClientboundPlayPayload(
		CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		addLateAction(bus ->
			bus.addListener(RegisterPayloadHandlersEvent.class, e -> {
				e.registrar("1").playToClient(type, codec);
			}));
	}

	@Override
	public <T extends CustomPacketPayload> void registerServerboundPlayPayload(
		CustomPacketPayload.Type<T> type,
		StreamCodec<RegistryFriendlyByteBuf, T> codec,
		PlayPacketReceiver<T> receiver
	) {
		addLateAction(bus ->
			bus.addListener(RegisterPayloadHandlersEvent.class, e -> {
				e.registrar("1")
					.playToServer(
						type,
						codec,
						(payload, context) -> {
							receiver.receive((ServerPlayer) context.player(), payload);
						});
			})
		);
	}

	@Override
	public boolean isClient() {
		return FMLEnvironment.getDist() == Dist.CLIENT;
	}

	public void addLateAction(Consumer<IEventBus> consumer) {
		if (EVENT_BUS != null) {
			consumer.accept(EVENT_BUS);
		} else {
			this.lateActions.add(consumer);
		}
	}
}
