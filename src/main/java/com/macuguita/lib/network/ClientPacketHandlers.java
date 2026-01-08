package com.macuguita.lib.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ClientPacketHandlers {

    private static final Map<CustomPacketPayload.Type<?>, Consumer<?>> HANDLERS = new HashMap<>();

    private ClientPacketHandlers() {}

    public static <T extends CustomPacketPayload> void register(
            CustomPacketPayload.Type<T> type,
            Consumer<T> handler
    ) {
        HANDLERS.put(type, handler);
    }

    @SuppressWarnings("unchecked")
    public static <T extends CustomPacketPayload> void handle(T payload) {
        Consumer<T> handler = (Consumer<T>) HANDLERS.get(payload.type());
        if (handler != null) {
            handler.accept(payload);
        }
    }
}
