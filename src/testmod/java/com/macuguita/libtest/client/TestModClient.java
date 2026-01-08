package com.macuguita.libtest.client;

import com.macuguita.lib.network.NetworkManager;
import com.macuguita.libtest.PingC2SPacket;
import com.macuguita.libtest.PingS2CPacket;
import com.macuguita.libtest.TestMod;
//? fabric {
/*import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
        *///?}

//? neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = TestMod.MOD_ID, value = Dist.CLIENT)
//?}
public class TestModClient {

    public static void init() {
        //? fabric {
        /*ClientPlayConnectionEvents.JOIN.register((listener, sender, client) -> {
            for (int i = 0; i < 10; i++) {
                TestMod.LOGGER.info("HELLO!!!");
            }
            NetworkManager.sendC2S(new PingC2SPacket(10));
        });
        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            for (var player : server.getPlayerList().getPlayers()) {
                NetworkManager.sendS2C(player, new PingS2CPacket(67));
            }
        });
        *///?}
        NetworkManager.registerS2C(
                PingS2CPacket.TYPE,
                PingS2CPacket.CODEC,
                (pkt, player) -> {
                    TestMod.LOGGER.info("PACKET VALUE: " + pkt.value());
                }
        );
    }

    //? neoforge {
    @SubscribeEvent
    public static void onClientLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        for (int i = 0; i < 10; i++) {
            TestMod.LOGGER.info("HELLO!!!");
        }
        NetworkManager.sendC2S(new PingC2SPacket(20));
    }
    //?}
}
