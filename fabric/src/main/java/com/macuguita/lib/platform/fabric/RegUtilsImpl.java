package com.macuguita.lib.platform.fabric;

import com.macuguita.lib.MacuguitaLib;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegUtilsImpl {

    public static List<Supplier<Block>> REGISTERED_BLOCKS = new ArrayList<>();

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        var registry = Registry.register(Registries.BLOCK, Identifier.of(MacuguitaLib.getModId(), name), block.get());
        REGISTERED_BLOCKS.add(() -> registry);
        return () -> registry;
    }
    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        var registry = Registry.register(Registries.ITEM, Identifier.of(MacuguitaLib.getModId(), name), item.get());
        return () -> registry;
    }
}
