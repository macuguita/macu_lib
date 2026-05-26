/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.network.PacketDistributor;
import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.api.reg.GuitaRegistries;
import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;

public class MacuLibTest {

    public static final String MOD_ID = "macu_lib_testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String name) {
        return Identifier.fromNamespaceAndPath(MOD_ID, name);
    }

    public static final GuitaRegistry<Block> BLOCKS =
        GuitaRegistries.create(BuiltInRegistries.BLOCK, MOD_ID);
    public static final GuitaRegistry<Item> ITEMS =
        GuitaRegistries.create(BuiltInRegistries.ITEM, MOD_ID);
    public static final GuitaRegistry<CreativeModeTab> CREATIVE_TAB =
        GuitaRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, MOD_ID);

    private static final GuitaRegistryEntry<CreativeModeTab> TAB =
        CREATIVE_TAB.register(
            MOD_ID,
            () ->
                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    // .displayItems(((_, output) -> BLOCKS.stream().forEach((regEntry) ->
                    // output.accept(regEntry.get().asItem()))))
                    .icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
                    .title(Component.literal("hello"))
                    .build());

    private static final GuitaRegistryEntry<Block> TEST_BLOCK =
        BLOCKS.register(
            "test_block",
            () ->
                new Block(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)
                        .setId(ResourceKey.create(Registries.BLOCK, id("test_block")))));

    public static void init() {
        ITEMS.register(
            "test_block",
            () ->
                new BlockItem(
                    TEST_BLOCK.get(),
                    new Item.Properties()
                        .useBlockDescriptionPrefix()
                        .setId(ResourceKey.create(Registries.ITEM, id("test_block")))));

        PacketRegistry.registerServerboundPlayPacket(
            PingServerboundPacket.TYPE,
            PingServerboundPacket.CODEC,
            (player, pkt) -> {
                LOGGER.info("CLIENT SENT: " + pkt.value());
            });

        PacketRegistry.registerClientboundPlayPacket(
            PingClientboundPacket.TYPE, PingClientboundPacket.CODEC);

        BLOCKS.init();
        ITEMS.init();
        CREATIVE_TAB.init();

        ServerPlayerJoinEvent.EVENT.register(
            (listener) -> {
                PacketDistributor.sendClientboundPacket(listener, new PingClientboundPacket(67));
            });
    }
}
