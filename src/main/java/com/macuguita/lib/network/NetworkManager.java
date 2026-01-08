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

package com.macuguita.lib.network;

import com.macuguita.lib.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility class for managing custom network packets between client and server.
 * <p>
 * Provides methods to register handlers for client-to-server (C2S) and
 * server-to-client (S2C) packets, as well as sending packets in both directions.
 * <p>
 * Example usage (from a mod initialization class):
 * <pre>{@code
 * // Registering a C2S packet
 * NetworkManager.registerC2S(MyPacket.TYPE, MyPacket.CODEC, (pkt, player) -> {
 *     // Handle packet received on server
 * });
 *
 * // Sending an S2C packet
 * NetworkManager.sendS2C(player, new MyPacket(...));
 * }</pre>
 */
public final class NetworkManager {

	private static final List<C2SRegistration<?>> C2S = new ArrayList<>();
	private static final List<S2CRegistration<?>> S2C = new ArrayList<>();

	private NetworkManager() {}

	/**
	 * Registers a client-to-server packet (C2S) with a lazily-supplied handler.
	 *
	 * @param type The {@link CustomPacketPayload.Type} of the packet.
	 * @param codec The {@link StreamCodec} used to serialize and deserialize the packet.
	 * @param handlerSupplier A {@link Supplier} of a {@link BiConsumer} handling the packet
	 *                        on the server. The BiConsumer receives the packet and the sending {@link ServerPlayer}.
	 * @param <T> The type of the packet.
	 */
	public static <T extends CustomPacketPayload> void registerC2S(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			Supplier<BiConsumer<T, ServerPlayer>> handlerSupplier
	) {
		C2SRegistration<T> reg = new C2SRegistration<>(type, codec, handlerSupplier);
		C2S.add(reg);
		Platform.INSTANCE.registerC2S(reg);
	}

	/**
	 * Registers a client-to-server packet (C2S) with a direct handler.
	 * <p>
	 * This is a convenience overload of {@link #registerC2S(CustomPacketPayload.Type, StreamCodec, Supplier)}.
	 */
	public static <T extends CustomPacketPayload> void registerC2S(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			BiConsumer<T, ServerPlayer> handlerSupplier
	) {
		registerC2S(type, codec, () -> handlerSupplier);
	}

	/**
	 * Sends a packet from the client to the server.
	 *
	 * @param payload The {@link CustomPacketPayload} to send.
	 */
	public static void sendC2S(CustomPacketPayload payload) {
		Platform.INSTANCE.sendToServer(payload);
	}

	/**
	 * Registers a server-to-client packet (S2C) with a lazily-supplied handler.
	 *
	 * @param type The {@link CustomPacketPayload.Type} of the packet.
	 * @param codec The {@link StreamCodec} used to serialize and deserialize the packet.
	 * @param handlerSupplier A {@link Supplier} of a {@link Consumer} handling the packet
	 *                        on the client.
	 * @param <T> The type of the packet.
	 */
	public static <T extends CustomPacketPayload> void registerS2C(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			Supplier<Consumer<T>> handlerSupplier
	) {
		S2CRegistration<T> reg = new S2CRegistration<>(type, codec, handlerSupplier);
		S2C.add(reg);
		Platform.INSTANCE.registerS2C(reg);
	}

	/**
	 * Registers a server-to-client packet (S2C) with a direct handler.
	 * <p>
	 * Convenience overload of {@link #registerS2C(CustomPacketPayload.Type, StreamCodec, Supplier)}.
	 */
	public static <T extends CustomPacketPayload> void registerS2C(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			Consumer<T> handlerSupplier
	) {
		registerS2C(type, codec, () -> handlerSupplier);
	}

	/**
	 * Sends a packet from the server to a specific player.
	 *
	 * @param player The {@link ServerPlayer} to receive the packet.
	 * @param payload The {@link CustomPacketPayload} to send.
	 */
	public static void sendS2C(ServerPlayer player, CustomPacketPayload payload) {
		Platform.INSTANCE.sendToPlayer(player, payload);
	}

	// Represents a registration of a client-to-server packet.
	@ApiStatus.Internal
	public record C2SRegistration<T extends CustomPacketPayload>(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			Supplier<BiConsumer<T, ServerPlayer>> handlerSupplier
	) {}

	// Represents a registration of a server-to-client packet.
	@ApiStatus.Internal
	public record S2CRegistration<T extends CustomPacketPayload>(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			Supplier<Consumer<T>> handlerSupplier
	) {}
}
