package com.macuguita.lib.neoforge.network;

import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID, value = Dist.CLIENT)
public class NeoForgeClientNetworkBootstrap {

    @SubscribeEvent
    public static void registerClientPackets(RegisterPayloadHandlersEvent event) {
        for (var reg : NeoForgeNetworkBootstrap.S2C) {
            registerClient(event, reg);
        }
    }

    private static <T extends CustomPacketPayload> void registerClient(
            RegisterPayloadHandlersEvent event,
            NetworkManager.S2CRegistration<T> reg
    ) {
        event.registrar(reg.type().id().getNamespace())
                .optional()
                .playToClient(
                        reg.type(),
                        reg.codec(),
                        (payload, ctx) -> reg.handler().handle(
                                payload,
                                Minecraft.getInstance().player
                        )
                );
    }
}
