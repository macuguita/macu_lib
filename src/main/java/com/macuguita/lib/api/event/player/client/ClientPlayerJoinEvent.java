/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.event.player.client;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.Identifier;

public interface ClientPlayerJoinEvent {

    /**
     * An event for notification when the client play packet listener is ready to send packets to the
     * server.
     *
     * <p>At this stage, the packet listener is ready to send packets to the server. Since the
     * client's local state has been set up.
     */
    Event<Identifier, ClientPlayerJoinEvent> EVENT =
        YumiEvents.EVENTS.create(
            ClientPlayerJoinEvent.class,
            listeners ->
                (packetListener, client) -> {
                    for (var listener : listeners) {
                        listener.playerJoin(packetListener, client);
                    }
                });

    void playerJoin(ClientPacketListener listener, Minecraft client);
}
