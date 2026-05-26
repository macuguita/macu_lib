/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.network;

import java.util.function.BiConsumer;

import org.apache.commons.lang3.function.TriConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import com.macuguita.lib.impl.platform.ClientAbstraction;
import com.macuguita.lib.impl.platform.CommonAbstraction;

/**
 * Handles registration of serverbound (C2S) and clientbound (S2C) payload types.
 *
 * @see PacketDistributor for sending packets.
 */
public final class PacketRegistry {

    private PacketRegistry() {}

    /**
     * Registers a serverbound (C2S) payload type and its server-side handler.
     *
     * <p>The handler is invoked on the server when a packet of this type is received, and is passed
     * the sending {@link ServerPlayer} and the decoded payload.
     *
     * @param type    the {@link CustomPacketPayload.Type} identifying this payload
     * @param codec   the {@link StreamCodec} used to encode and decode the payload
     * @param handler a {@link BiConsumer} invoked on the server with the sending player and payload
     * @param <T>     the payload type
     */
    public static <T extends CustomPacketPayload> void registerServerboundPlayPacket(
        CustomPacketPayload.Type<T> type,
        StreamCodec<RegistryFriendlyByteBuf, T> codec,
        BiConsumer<ServerPlayer, T> handler) {
        CommonAbstraction.get().registerServerboundPlayPayload(type, codec, handler::accept);
    }

    /**
     * Registers a clientbound (S2C) payload type without attaching a client-side handler.
     *
     * <p>This only declares the payload type and codec so the platform can recognize and decode
     * incoming packets. To handle them on the client, call {@link #registerClientboundPacketHandler}
     * from a client-only code path after registration.
     *
     * @param type  the {@link CustomPacketPayload.Type} identifying this payload
     * @param codec the {@link StreamCodec} used to encode and decode the payload
     * @param <T>   the payload type
     */
    public static <T extends CustomPacketPayload> void registerClientboundPlayPacket(
        CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        CommonAbstraction.get().registerClientboundPlayPayload(type, codec);
    }

    /**
     * Attaches a client-side handler to an already-registered clientbound (S2C) payload type.
     *
     * <p><strong>This method must only be called from client-only code</strong> (e.g. a client
     * lifecycle event or client-only entrypoint). Calling it on a dedicated server will cause a crash
     * or classloading error.
     *
     * <p>The payload type must have been previously registered via {@link
     * #registerClientboundPlayPacket}.
     *
     * @param type    the {@link CustomPacketPayload.Type} of the payload to handle
     * @param handler a {@link TriConsumer} invoked on the client with the {@link Minecraft} instance,
     *                the local {@link LocalPlayer}, and the received payload
     * @param <T>     the payload type
     */
    public static <T extends CustomPacketPayload> void registerClientboundPacketHandler(
        CustomPacketPayload.Type<T> type, TriConsumer<Minecraft, LocalPlayer, T> handler) {
        ClientAbstraction.get().registerGlobalReceiverPlay(type, handler::accept);
    }
}
