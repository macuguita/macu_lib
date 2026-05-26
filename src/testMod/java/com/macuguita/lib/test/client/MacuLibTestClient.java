/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test.client;

import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.network.PacketDistributor;
import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.test.MacuLibTest;
import com.macuguita.lib.test.PingClientboundPacket;
import com.macuguita.lib.test.PingServerboundPacket;

public class MacuLibTestClient implements ClientModInitializer {

    @Override
    public void onInitializeClient(ModContainer mod) {
        PacketRegistry.registerClientboundPacketHandler(
            PingClientboundPacket.TYPE,
            (_, _, pkt) -> MacuLibTest.LOGGER.info("SERVER SENT: {}", pkt.value()));

        ClientPlayerJoinEvent.EVENT.register(
            (_, _) -> PacketDistributor.sendServerboundPacket(new PingServerboundPacket(420)));
    }
}
