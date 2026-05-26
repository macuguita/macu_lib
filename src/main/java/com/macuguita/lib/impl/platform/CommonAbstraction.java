/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
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
