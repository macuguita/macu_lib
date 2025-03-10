package com.macuguita.lib.platform.registry.builtin;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.builtin.base.ItemConvertibleEntry;
import com.macuguita.lib.platform.registry.builtin.base.ItemConvertibleHolderEntryGuita;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class GuitaItemRegistry implements GuitaRegistry<Item> {

    private final String namespace;
    private final GuitaRegistry<Item> registry;

    public GuitaItemRegistry(String id) {
        this(GuitaRegistries.create(Registries.ITEM, id));
    }

    public GuitaItemRegistry(GuitaRegistry<Item> parent) {
        this.namespace = Objects.requireNonNull(parent.namespace(), "Parent registry must have a namespace.");
        this.registry = parent;
    }

    public <I extends Item> ItemConvertibleEntry<I> register(String id, Function<Item.Settings, I> factory, Supplier<Item.Settings> getter) {
        RegistryKey<Item> key = RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(this.namespace, id));
        return this.register(id, () -> factory.apply(getter.get().registryKey(key)));
    }

    public ItemConvertibleEntry<BlockItem> register(String id, Supplier<? extends Block> supplier, Supplier<Item.Settings> getter) {
        return register(id, properties -> new BlockItem(supplier.get(), properties.useBlockPrefixedTranslationKey()), getter);
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public <I extends Item> ItemConvertibleEntry<I> register(String id, Supplier<I> supplier) {
        return new ItemConvertibleEntry<>(this.registry.register(id, supplier));
    }

    @Override
    public ItemConvertibleHolderEntryGuita<Item> registerRegistryEntry(String id, Supplier<Item> supplier) {
        return new ItemConvertibleHolderEntryGuita<>(this.registry.registerRegistryEntry(id, supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<Item>> getEntries() {
        return this.registry.getEntries();
    }

    @Override
    public void init() {
        this.registry.init();
    }
}
