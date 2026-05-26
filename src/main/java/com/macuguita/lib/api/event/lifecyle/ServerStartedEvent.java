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

public interface ServerStartedEvent {

    /**
     * Called when a Minecraft server has started and is about to tick for the first time.
     *
     * <p>At this stage, all levels are live.
     */
    Event<Identifier, ServerStartedEvent> EVENT =
        YumiEvents.EVENTS.create(
            ServerStartedEvent.class,
            listeners ->
                (server) -> {
                    for (var listener : listeners) {
                        listener.serverStarted(server);
                    }
                });

    void serverStarted(MinecraftServer server);
}
