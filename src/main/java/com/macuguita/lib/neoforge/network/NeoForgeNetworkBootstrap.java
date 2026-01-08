package com.macuguita.lib.neoforge.network;

//? neoforge {
import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID)
public final class NeoForgeNetworkBootstrap {

    public static final List<NetworkManager.C2SRegistration<?>> C2S = new ArrayList<>();
    public static final List<NetworkManager.S2CRegistration<?>> S2C = new ArrayList<>();

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        for (var reg : C2S) {
            registerC2S(event, reg);
        }

        for (var reg : S2C) {
            registerS2C(event, reg);
        }
    }

    private static <T extends CustomPacketPayload> void registerC2S(
            RegisterPayloadHandlersEvent event,
            NetworkManager.C2SRegistration<T> reg
    ) {
        event.registrar(reg.type().id().getNamespace())
                .optional()
                .playToServer(
                        reg.type(),
                        reg.codec(),
                        (payload, ctx) -> reg.handler().handle(
                                payload,
                                (ServerPlayer) ctx.player()
                        )
                );
    }

    private static <T extends CustomPacketPayload> void registerS2C(
            RegisterPayloadHandlersEvent event,
            NetworkManager.S2CRegistration<T> reg
    ) {
        event.registrar(reg.type().id().getNamespace())
                .optional()
                .playToClient(
                        reg.type(),
                        reg.codec()
                );
    }
}
//?}