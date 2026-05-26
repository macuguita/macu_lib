/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform;

import dev.yumi.commons.event.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;

@ApiStatus.Internal
public interface ClientAbstraction {

    ClientAbstraction INSTANCE =
        Util.make(
            () -> {
                try {
                    return (ClientAbstraction)
                        Class.forName(
                                "com.macuguita.lib.impl.platform."
                                    + (CommonAbstraction.IS_FABRIC
                                    ? "fabric.FabricClientAbstraction"
                                    : "neoforge.NeoClientAbstraction"))
                            .getField("INSTANCE")
                            .get(null);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            });

    static ClientAbstraction get() {
        return INSTANCE;
    }

    // @formatter:off
  void registerPlayerJoinEvent(Event<Identifier, ClientPlayerJoinEvent> event);

  void registerPlayerLeaveEvent(Event<Identifier, ClientPlayerLeaveEvent> event);

  // @formatter:on

    <T extends CustomPacketPayload> void registerGlobalReceiverPlay(
        CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver);

    interface PlayPacketReceiver<T> {
        void receive(Minecraft minecraft, LocalPlayer player, T payload);
    }
}
