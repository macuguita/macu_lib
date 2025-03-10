package com.macuguita.lib.platform.registry.builtin;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.builtin.base.ItemConvertibleEntry;
import com.macuguita.lib.platform.registry.builtin.base.ItemConvertibleHolderEntryGuita;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class GuitaBlockRegistry implements GuitaRegistry<Block> {

    private final String namespace;
    private final GuitaRegistry<Block> registry;

    public GuitaBlockRegistry(String id) {
        this(GuitaRegistries.create(Registries.BLOCK, id));
    }

    public GuitaBlockRegistry(GuitaRegistry<Block> parent) {
        this.namespace = Objects.requireNonNull(parent.namespace(), "Parent registry must have a namespace.");
        this.registry = parent;
    }

    public <I extends Block> ItemConvertibleEntry<I> register(String id, Function<AbstractBlock.Settings, I> factory, Supplier<Block.Settings> getter) {
        RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), Identifier.of(this.namespace, id));
        return this.register(id, () -> factory.apply(getter.get().registryKey(key)));
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public <I extends Block> ItemConvertibleEntry<I> register(String id, Supplier<I> supplier) {
        return new ItemConvertibleEntry<>(this.registry.register(id, supplier));
    }

    @Override
    public ItemConvertibleHolderEntryGuita<Block> registerRegistryEntry(String id, Supplier<Block> supplier) {
        return new ItemConvertibleHolderEntryGuita<>(this.registry.registerRegistryEntry(id, supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<Block>> getEntries() {
        return this.registry.getEntries();
    }

    @Override
    public void init() {
        this.registry.init();
    }
}
