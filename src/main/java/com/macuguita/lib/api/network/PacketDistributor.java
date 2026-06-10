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
package com.macuguita.lib.api.network;

import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/**
 * Handles sending serverbound (C2S) and clientbound (S2C) packets.
 *
 * @see PacketRegistry for registering payload types and handlers.
 */
public final class PacketDistributor {

	private PacketDistributor() {}

	/**
	 * Sends a serverbound (C2S) payload from the client to the server.
	 *
	 * <p>Must only be called from the client while a server connection is active (i.e. the player is
	 * in a world). Throws {@link IllegalStateException} if there is no active connection.
	 *
	 * @param payload the {@link CustomPacketPayload} to send
	 * @throws IllegalStateException if there is no active server connection
	 */
	public static void sendServerboundPacket(CustomPacketPayload payload) {
		Objects.requireNonNull(payload, "Payload cannot be null");
		Objects.requireNonNull(payload.type(), "CustomPacketPayload#type() cannot return null for payload class: " + payload.getClass());

		if (Minecraft.getInstance().getConnection() != null) {
			Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(payload));
			return;
		}

		throw new IllegalStateException("Cannot send packets when not connected to a server!");
	}

	/**
	 * Sends a clientbound (S2C) payload from the server to a specific client.
	 *
	 * @param player  the {@link ServerPlayer} to receive the payload
	 * @param payload the {@link CustomPacketPayload} to send
	 */
	public static void sendClientboundPacket(ServerPlayer player, CustomPacketPayload payload) {
		Objects.requireNonNull(player, "Server player cannot be null");
		Objects.requireNonNull(payload, "Payload cannot be null");
		Objects.requireNonNull(payload.type(), "CustomPacketPayload#type() cannot return null for payload class: " + payload.getClass());

		player.connection.send(new ClientboundCustomPayloadPacket(payload));
	}
}
