/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.client;

import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;
import com.macuguita.lib.impl.platform.ClientAbstraction;

public class MacuLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        registerEvents();
    }

    private void registerEvents() {
        ClientAbstraction.get().registerPlayerJoinEvent(ClientPlayerJoinEvent.EVENT);
        ClientAbstraction.get().registerPlayerLeaveEvent(ClientPlayerLeaveEvent.EVENT);
    }
}
