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

package com.macuguita.libtest;

import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistries;
import com.macuguita.lib.reg.GuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistryEntry;
//? fabric {
/*import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
*///?}
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
//? neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
//?}
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMod {

    public static final String MOD_ID = "macu_lib_tests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final GuitaRegistry<Block> BLOCKS = GuitaRegistries.create(BuiltInRegistries.BLOCK, MOD_ID);
    public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(BuiltInRegistries.ITEM, MOD_ID);
    public static final GuitaRegistry<CreativeModeTab> CREATIVE_TAB = GuitaRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_ID);

    private static final GuitaRegistryEntry<CreativeModeTab> TAB = CREATIVE_TAB.register(MOD_ID, () -> CreativeModeTab.builder(/*? fabric {*/ /*CreativeModeTab.Row.TOP, 0 *//*?}*/)
            .displayItems(((itemDisplayParameters, output) -> BLOCKS.stream().forEach((regEntry) -> output.accept(regEntry.get().asItem()))))
            .icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
            .title(Component.literal("hello"))
            .build());

    private static final GuitaRegistryEntry<Block> TEST_BLOCK =
            BLOCKS.register("test_block", () ->
                    new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)
                            /*? >= 1.21.11 {*/.setId(ResourceKey.create(Registries.BLOCK, id("test_block")))/*?}*/));

    public static void init() {
        ITEMS.register("test_block", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties()
				//? >= 1.21.11 {
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, id("test_block")))
				//?}
		));

        NetworkManager.registerC2S(
                PingC2SPacket.TYPE,
                PingC2SPacket.CODEC,
                (pkt, player) -> {
                    TestMod.LOGGER.info("CLIENT SENT: " + pkt.value());
                }
        );

        NetworkManager.registerS2C(
                PingS2CPacket.TYPE,
                PingS2CPacket.CODEC,
                pkt -> TestMod.LOGGER.info("SERVER SENT: {}", pkt.value())
        );

        NetworkManager.registerS2C( // test for missing handler
				PingS2CPacketNoHandler.TYPE,
				PingS2CPacketNoHandler.CODEC
        );

        BLOCKS.init();
        ITEMS.init();
        CREATIVE_TAB.init();
        //? fabric {
        /*ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            NetworkManager.sendS2C(listener.player, new PingS2CPacket(67));
        });
        *///?}
    }

    //? neoforge {
    @EventBusSubscriber(modid = TestMod.MOD_ID)
    public static class CommonEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                NetworkManager.sendS2C(serverPlayer, new PingS2CPacket(69));
            }
        }
    }
    //?}
}
