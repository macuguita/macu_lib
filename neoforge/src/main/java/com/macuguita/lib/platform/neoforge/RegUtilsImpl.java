package com.macuguita.lib.platform.neoforge;

import com.macuguita.lib.MacuguitaLib;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RegUtilsImpl {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MacuguitaLib.getModId());
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(MacuguitaLib.getModId());

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }
}