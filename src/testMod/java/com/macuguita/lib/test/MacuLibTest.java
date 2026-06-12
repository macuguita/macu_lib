/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.network.PacketDistributor;
import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.api.reg.GuitaHolderRegistryEntry;
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

	private static final GuitaHolderRegistryEntry<CreativeModeTab> TAB =
		CREATIVE_TAB.registerForHolder(
			MOD_ID,
			() ->
				CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
					.icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
					.displayItems(((parameters, output) -> {
						output.accept(Items.DIRT);
					}))
					.title(Component.literal("hello"))
					.build());

	private static final GuitaRegistryEntry<Block> TEST_BLOCK =
		BLOCKS.register(
			"test_block",
			() ->
				new Block(
					BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)
						.setId(ResourceKey.create(Registries.BLOCK, id("test_block")))));

	// Have to do this because I cannot use a yumi entrypoint on the test mod
	// since I need to test how normal mods would behave.
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
			(_, pkt) -> {
				LOGGER.info("CLIENT SENT: {}", pkt.value());
			});

		PacketRegistry.registerClientboundPlayPacket(
			PingClientboundPacket.TYPE, PingClientboundPacket.CODEC);

		BLOCKS.init();
		ITEMS.init();
		CREATIVE_TAB.init();

		ModifyCreativeTabOutputEvent.forTab(ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("combat")))
			.register((_, output) -> {
				output.insertAfter(Items.DIAMOND_SWORD, TEST_BLOCK.get());
			});

		//noinspection OptionalGetWithoutIsPresent
		ModifyCreativeTabOutputEvent.forTab(TAB.holder().unwrapKey().get()).register((_, output) -> {
			BLOCKS.stream().forEach((regEntry) ->
				output.accept(regEntry.get().asItem()));
		});

		ServerPlayerJoinEvent.EVENT.register(
			(listener) -> {
				PacketDistributor.sendClientboundPacket(listener, new PingClientboundPacket(67));
			});
	}
}
