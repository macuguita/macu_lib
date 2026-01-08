package com.macuguita.libtest;

import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistries;
import com.macuguita.lib.reg.GuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistryEntry;
import com.macuguita.libtest.client.TestModClient;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
            .displayItems(((_, output) -> BLOCKS.stream().forEach((regEntry) -> output.accept(regEntry.get().asItem()))))
            .icon(() -> new ItemStack(Blocks.DIAMOND_BLOCK))
            .title(Component.literal("hello"))
            .build());

    private static final GuitaRegistryEntry<Block> TEST_BLOCK =
            BLOCKS.register("test_block", () ->
                    new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)
                            .setId(ResourceKey.create(Registries.BLOCK, id("test_block")))));

    public static void init() {
        ITEMS.register("test_block", () -> new BlockItem(TEST_BLOCK.get(), new Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(ResourceKey.create(Registries.ITEM, id("test_block")))));

        NetworkManager.registerC2S(
                PingC2SPacket.TYPE,
                PingC2SPacket.CODEC,
                (pkt, player) -> {
                    TestMod.LOGGER.info("PACKET VALUE: " + pkt.value());
                }
        );

        BLOCKS.init();
        ITEMS.init();
        CREATIVE_TAB.init();
    }

    //? neoforge {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.sendC2S(new PingC2SPacket(69));
    }
    //?}
}
