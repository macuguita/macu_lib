package com.macuguita.lib.platform.registry.builtin.base;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record ItemConvertibleEntry<T extends ItemConvertible>(GuitaRegistryEntry<T> entry) implements GuitaRegistryEntry<T>, ItemConvertible {

    @Override
    public T get() {
        return entry.get();
    }

    @Override
    public Identifier getId() {
        return entry.getId();
    }

    @Override
    @NotNull
    public Item asItem() {
        return get().asItem();
    }
}