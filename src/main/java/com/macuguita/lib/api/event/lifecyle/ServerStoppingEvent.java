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

public interface ServerStoppingEvent {

    /**
     * Called when a Minecraft server has started shutting down. This occurs before the server's
     * network channel is closed and before any players are disconnected.
     *
     * <p>For example, an integrated server will begin stopping, but its client may continue to run.
     *
     * <p>All levels are still present and can be modified.
     */
    Event<Identifier, ServerStoppingEvent> EVENT =
        YumiEvents.EVENTS.create(
            ServerStoppingEvent.class,
            listeners ->
                (server) -> {
                    for (var listener : listeners) {
                        listener.serverStopping(server);
                    }
                });

    void serverStopping(MinecraftServer server);
}
