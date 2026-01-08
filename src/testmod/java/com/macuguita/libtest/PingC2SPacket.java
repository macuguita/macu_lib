package com.macuguita.libtest;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PingC2SPacket(int value)
		implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<PingC2SPacket> TYPE =
			new Type<>(TestMod.id("ping_c2s"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PingC2SPacket> CODEC =
			StreamCodec.of(
					(buf, pkt) -> buf.writeInt(pkt.value()),
					buf -> new PingC2SPacket(buf.readInt())
			);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

