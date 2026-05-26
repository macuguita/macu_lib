/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform.fabric;

import dev.yumi.commons.event.Event;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;
import com.macuguita.lib.impl.platform.ClientAbstraction;

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
        CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver) {
        ClientPlayNetworking.registerGlobalReceiver(
            type, (p, ctx) -> receiver.receive(ctx.client(), ctx.player(), p));
    }
}
