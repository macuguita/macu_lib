package com.macuguita.libtest;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PingS2CPacket(int value)
        implements CustomPacketPayload {

    public static final Type<PingS2CPacket> TYPE =
            new Type<>(TestMod.id("ping_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PingS2CPacket> CODEC =
            StreamCodec.of(
                    (buf, pkt) -> buf.writeInt(pkt.value()),
                    buf -> new PingS2CPacket(buf.readInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

