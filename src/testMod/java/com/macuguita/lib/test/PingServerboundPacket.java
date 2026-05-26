/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PingServerboundPacket(int value) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<PingServerboundPacket> TYPE =
        new Type<>(MacuLibTest.id("ping_c2s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PingServerboundPacket> CODEC =
        StreamCodec.of(
            (buf, pkt) -> buf.writeInt(pkt.value()), buf -> new PingServerboundPacket(buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
