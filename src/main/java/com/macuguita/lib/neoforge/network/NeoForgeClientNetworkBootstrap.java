package com.macuguita.lib.neoforge.network;

//? neoforge {

import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.ClientPacketHandlers;
import com.macuguita.lib.network.NetworkManager;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientNetworkBootstrap {

    @SubscribeEvent
    public static void registerClientHandlers(RegisterClientPayloadHandlersEvent event) {
        for (NetworkManager.S2CRegistration<?> reg : NeoForgeNetworkBootstrap.S2C) {
            registerS2CHandler(event, reg);
        }
    }

    private static <T extends CustomPacketPayload> void registerS2CHandler(
            RegisterClientPayloadHandlersEvent event,
            NetworkManager.S2CRegistration<T> reg
    ) {
        event.register(reg.type(), (payload, context) -> {
            ClientPacketHandlers.handle(payload);
        });
    }
}
//?}