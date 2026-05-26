/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform.neoforge;

import dev.yumi.commons.event.Event;

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

public record NeoClientAbstraction() implements ClientAbstraction {
    public static final NeoClientAbstraction INSTANCE = new NeoClientAbstraction();

    @Override
    public void registerPlayerJoinEvent(Event<Identifier, ClientPlayerJoinEvent> event) {
        NeoForge.EVENT_BUS.addListener(
            ClientPlayerNetworkEvent.LoggingIn.class,
            e -> {
                event.invoker().playerJoin(e.getPlayer().connection, Minecraft.getInstance());
            });
    }

    @Override
    public void registerPlayerLeaveEvent(Event<Identifier, ClientPlayerLeaveEvent> event) {
        NeoForge.EVENT_BUS.addListener(
            ClientPlayerNetworkEvent.LoggingOut.class,
            e -> {
                if (e.getPlayer() == null) return;
                event.invoker().playerLeave(e.getPlayer().connection, Minecraft.getInstance());
            });
    }

    @Override
    public <T extends CustomPacketPayload> void registerGlobalReceiverPlay(
        CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver) {
        NeoCommonAbstraction.INSTANCE.addLateAction(
            bus -> {
                bus.addListener(
                    RegisterClientPayloadHandlersEvent.class,
                    e -> {
                        e.register(
                            type,
                            (p, ctx) ->
                                receiver.receive(Minecraft.getInstance(), (LocalPlayer) ctx.player(), p));
                    });
            });
    }
}
