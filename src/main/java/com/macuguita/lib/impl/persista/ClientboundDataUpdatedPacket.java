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
public record ClientboundDataUpdatedPacket(UUID playerId, Identifier dataId) implements CustomPacketPayload {

	public static final Identifier CLIENTBOUND_DATA_UPDATE =
		MacuLib.id("clientbound_data_updated");
	public static final CustomPacketPayload.Type<ClientboundDataUpdatedPacket> TYPE =
		new CustomPacketPayload.Type<>(CLIENTBOUND_DATA_UPDATE);

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDataUpdatedPacket> CODEC = StreamCodec.composite(
		UUIDUtil.STREAM_CODEC,
		ClientboundDataUpdatedPacket::playerId,
		Identifier.STREAM_CODEC,
		ClientboundDataUpdatedPacket::dataId,
		ClientboundDataUpdatedPacket::new
	);

	public static void send(ServerPlayer player, UUID targetId, Identifier dataId) {
		PacketDistributor.sendClientboundPacket(player, new ClientboundDataUpdatedPacket(targetId, dataId));
	}

	public static void handle(Minecraft mc, LocalPlayer player, ClientboundDataUpdatedPacket pkt) {
		var entry = DataRegistry.getById(pkt.dataId);
		entry.ifPresentOrElse(
			entryx -> DataCache.lookup(pkt.playerId, entryx, true),
			() -> Persista.LOGGER.debug("Ignoring sync for unknown data type: {}", pkt.dataId)
		);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
