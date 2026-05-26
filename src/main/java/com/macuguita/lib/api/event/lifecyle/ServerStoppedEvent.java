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

public interface ServerStoppedEvent {

    /**
     * Called when a Minecraft server has stopped. All levels have been closed and all (block)entities
     * and players have been unloaded.
     *
     * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin
     * stopping, but its client may continue to run. Meanwhile, for a {@link
     * net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
     */
    Event<Identifier, ServerStoppedEvent> EVENT =
        YumiEvents.EVENTS.create(
            ServerStoppedEvent.class,
            listeners ->
                (server) -> {
                    for (var listener : listeners) {
                        listener.serverStopped(server);
                    }
                });

    void serverStopped(MinecraftServer server);
}
