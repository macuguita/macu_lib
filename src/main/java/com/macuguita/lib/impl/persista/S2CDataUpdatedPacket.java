/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.persista;

import java.util.UUID;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import com.macuguita.lib.api.network.PacketDistributor;
import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
public record S2CDataUpdatedPacket(UUID playerId, Identifier dataId) implements CustomPacketPayload {

	public static final Identifier CLIENTBOUND_DATA_UPDATE =
		MacuLib.id("s2c_data_updated");
	public static final CustomPacketPayload.Type<S2CDataUpdatedPacket> TYPE =
		new CustomPacketPayload.Type<>(CLIENTBOUND_DATA_UPDATE);

	public static final StreamCodec<RegistryFriendlyByteBuf, S2CDataUpdatedPacket> CODEC = StreamCodec.composite(
		UUIDUtil.STREAM_CODEC,
		S2CDataUpdatedPacket::playerId,
		Identifier.STREAM_CODEC,
		S2CDataUpdatedPacket::dataId,
		S2CDataUpdatedPacket::new
	);

	public static void send(ServerPlayer player, UUID targetId, Identifier dataId) {
		PacketDistributor.sendClientboundPacket(player, new S2CDataUpdatedPacket(targetId, dataId));
	}

	public static void handle(Minecraft mc, LocalPlayer player, S2CDataUpdatedPacket pkt) {
		DataEntry<?> entry = DataRegistry.getById(pkt.dataId);
		if (entry == null) {
			PersistaLogger.get().debug("Ignoring sync for unknown data type: {}", pkt.dataId);
			return;
		}
		DataCache.lookup(pkt.playerId, entry, true);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
