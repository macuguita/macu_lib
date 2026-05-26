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

public interface ClientPlayerLeaveEvent {

    /**
     * An event for the disconnection of the client play packet listener.
     *
     * <p>No packets should be sent when this event is invoked. The player may be null.
     */
    Event<Identifier, ClientPlayerLeaveEvent> EVENT =
        YumiEvents.EVENTS.create(
            ClientPlayerLeaveEvent.class,
            listeners ->
                (packetListener, client) -> {
                    for (var listener : listeners) {
                        listener.playerLeave(packetListener, client);
                    }
                });

    void playerLeave(ClientPacketListener listener, Minecraft client);
}
