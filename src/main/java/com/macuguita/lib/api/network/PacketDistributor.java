/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
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
        Objects.requireNonNull(
            payload.type(),
            "CustomPacketPayload#type() cannot return null for payload class: " + payload.getClass());

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
        Objects.requireNonNull(
            payload.type(),
            "CustomPacketPayload#type() cannot return null for payload class: " + payload.getClass());

        player.connection.send(new ClientboundCustomPayloadPacket(payload));
    }
}
