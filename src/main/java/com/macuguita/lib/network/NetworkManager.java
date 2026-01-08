package com.macuguita.lib.network;

import com.macuguita.lib.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class NetworkManager {

    private static final List<C2SRegistration<?>> C2S = new ArrayList<>();
    private static final List<S2CRegistration<?>> S2C = new ArrayList<>();

    private NetworkManager() {}

    /* ---------------- C2S ---------------- */

    public static <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            C2SHandler<T> handler
    ) {
        C2SRegistration<T> reg = new C2SRegistration<>(type, codec, handler);
        C2S.add(reg);
        Platform.INSTANCE.registerC2S(reg);
    }

    public static void sendC2S(CustomPacketPayload payload) {
        Platform.INSTANCE.sendToServer(payload);
    }

    /* ---------------- S2C ---------------- */

    /**
     * Registers ONLY the packet type + codec.
     * Handler is client-only and registered separately.
     */
    public static <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec
    ) {
        S2CRegistration<T> reg = new S2CRegistration<>(type, codec);
        S2C.add(reg);
        Platform.INSTANCE.registerS2C(reg);
    }

    public static void sendS2C(ServerPlayer player, CustomPacketPayload payload) {
        Platform.INSTANCE.sendToPlayer(player, payload);
    }

    /* ---------------- INTERNAL ---------------- */

    public static List<S2CRegistration<?>> getS2CRegistrations() {
        return S2C;
    }

    /* ---------------- TYPES ---------------- */

    public record C2SRegistration<T extends CustomPacketPayload>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            C2SHandler<T> handler
    ) {}

    public record S2CRegistration<T extends CustomPacketPayload>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec
    ) {}

    @FunctionalInterface
    public interface C2SHandler<T extends CustomPacketPayload> {
        void handle(T payload, ServerPlayer player);
    }
}
