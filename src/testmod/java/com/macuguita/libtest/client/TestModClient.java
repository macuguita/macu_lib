package com.macuguita.libtest.client;

import com.macuguita.lib.network.NetworkManager;
import com.macuguita.libtest.PingC2SPacket;
import com.macuguita.libtest.TestMod;
//? fabric {
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
//?}
//? neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = TestMod.MOD_ID, value = Dist.CLIENT)
*///?}
public class TestModClient {

    public static void init() {
        //? fabric {
        ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
            for (int i = 0; i < 10; i++) {
                TestMod.LOGGER.info("HELLO!!!");
            }
            NetworkManager.sendC2S(new PingC2SPacket(21));
        });
        //?}
    }

    //? neoforge {
    /*@SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        for (int i = 0; i < 10; i++) {
            TestMod.LOGGER.info("HELLO!!!");
        }
        NetworkManager.sendC2S(new PingC2SPacket(420));
    }
    *///?}
}
