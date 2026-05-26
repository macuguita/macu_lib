/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.event.lifecyle;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;

public interface ServerStartingEvent {

    /**
     * Called when a Minecraft server is starting.
     *
     * <p>This occurs before the {@link PlayerList player list} and any levels are loaded.
     */
    Event<Identifier, ServerStartingEvent> EVENT =
        YumiEvents.EVENTS.create(
            ServerStartingEvent.class,
            listeners ->
                (server) -> {
                    for (var listener : listeners) {
                        listener.serverStarting(server);
                    }
                });

    void serverStarting(MinecraftServer server);
}
