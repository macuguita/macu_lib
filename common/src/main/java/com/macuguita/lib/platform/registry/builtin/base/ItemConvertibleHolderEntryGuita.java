package com.macuguita.lib.platform.registry.builtin.base;

import com.macuguita.lib.platform.registry.RegistryEntryGuitaRegistryEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record ItemConvertibleHolderEntryGuita<T extends ItemConvertible>(RegistryEntryGuitaRegistryEntry<T> entry) implements RegistryEntryGuitaRegistryEntry<T>, ItemConvertible {

    @Override
    public RegistryEntry<T> registryEntry() {
        return entry.registryEntry();
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