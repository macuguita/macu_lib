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

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import com.macuguita.lib.api.network.PacketDistributor;
import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
public record ServerboundDataUpdatedPacket(Identifier dataId) implements CustomPacketPayload {

	public static final Identifier SERVERBOUND_DATA_UPDATED =
		MacuLib.id("serverbound_data_updated");
	public static final CustomPacketPayload.Type<ServerboundDataUpdatedPacket> TYPE =
		new CustomPacketPayload.Type<>(SERVERBOUND_DATA_UPDATED);

	public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDataUpdatedPacket> CODEC = StreamCodec.composite(
		Identifier.STREAM_CODEC,
		ServerboundDataUpdatedPacket::dataId,
		ServerboundDataUpdatedPacket::new
	);

	public static void trySend(Identifier dataId) {
		try {
			PacketDistributor.sendServerboundPacket(new ServerboundDataUpdatedPacket(dataId));
		} catch (IllegalStateException ignored) {
			// singleplayer / no server
		}
	}

	public static void handle(ServerPlayer sender, ServerboundDataUpdatedPacket pkt) {
		UUID playerId = sender.getGameProfile().id();

		DataEntry<?> entry = DataRegistry.getById(pkt.dataId);
		if (entry != null) {
			DataCache.lookup(playerId, entry, true);
		}

		//noinspection resource
		sender.level().getServer().getPlayerList().getPlayers().stream()
			.filter(p -> p != sender)
			.forEach(p -> ClientboundDataUpdatedPacket.send(p, playerId, pkt.dataId));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
