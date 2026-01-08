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
