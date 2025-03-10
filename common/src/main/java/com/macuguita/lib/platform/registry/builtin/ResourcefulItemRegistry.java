package com.macuguita.lib.platform.registry.builtin;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.ResourcefulRegistries;
import com.macuguita.lib.platform.registry.ResourcefulRegistry;
import com.macuguita.lib.platform.registry.builtin.base.ItemLikeEntry;
import com.macuguita.lib.platform.registry.builtin.base.ItemLikeHolderEntryGuita;
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

public class ResourcefulItemRegistry implements ResourcefulRegistry<Item> {

    private final String namespace;
    private final ResourcefulRegistry<Item> registry;

    public ResourcefulItemRegistry(String id) {
        this(ResourcefulRegistries.create(Registries.ITEM, id));
    }

    public ResourcefulItemRegistry(ResourcefulRegistry<Item> parent) {
        this.namespace = Objects.requireNonNull(parent.namespace(), "Parent registry must have a namespace.");
        this.registry = parent;
    }

    public <I extends Item> ItemLikeEntry<I> register(String id, Function<Item.Settings, I> factory, Supplier<Item.Settings> getter) {
        RegistryKey<Item> key = RegistryKey.of(Registries.ITEM.getKey(), Identifier.of(this.namespace, id));
        return this.register(id, () -> factory.apply(getter.get().registryKey(key)));
    }

    public ItemLikeEntry<BlockItem> register(String id, Supplier<? extends Block> supplier, Supplier<Item.Settings> getter) {
        return register(id, properties -> new BlockItem(supplier.get(), properties.useBlockPrefixedTranslationKey()), getter);
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public <I extends Item> ItemLikeEntry<I> register(String id, Supplier<I> supplier) {
        return new ItemLikeEntry<>(this.registry.register(id, supplier));
    }

    @Override
    public ItemLikeHolderEntryGuita<Item> registerHolder(String id, Supplier<Item> supplier) {
        return new ItemLikeHolderEntryGuita<>(this.registry.registerHolder(id, supplier));
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
