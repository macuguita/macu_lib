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

public record PingClientboundPacket(int value) implements CustomPacketPayload {

    public static final Type<PingClientboundPacket> TYPE = new Type<>(MacuLibTest.id("ping_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PingClientboundPacket> CODEC =
        StreamCodec.of(
            (buf, pkt) -> buf.writeInt(pkt.value()), buf -> new PingClientboundPacket(buf.readInt()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
