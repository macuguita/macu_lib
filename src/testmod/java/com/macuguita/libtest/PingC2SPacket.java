/*
 * macu_lib
 * Copyright (C) 2026 macuguita
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

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

