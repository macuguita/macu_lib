/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.event.player.server;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerJoinEvent {

    /**
     * An event that is called when a player has joined the game. This includes loading a singleplayer
     * world.
     *
     * <p>This event is called on the server thread after the player has fully been loaded into the
     * world.
     */
    Event<Identifier, ServerPlayerJoinEvent> EVENT =
        YumiEvents.EVENTS.create(
            ServerPlayerJoinEvent.class,
            listeners ->
                (serverPlayer) -> {
                    for (var listener : listeners) {
                        listener.playerJoin(serverPlayer);
                    }
                });

    void playerJoin(ServerPlayer serverPlayer);
}
