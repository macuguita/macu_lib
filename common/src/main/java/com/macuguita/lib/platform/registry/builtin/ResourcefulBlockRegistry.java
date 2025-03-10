package com.macuguita.lib.platform.registry.builtin;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.ResourcefulRegistries;
import com.macuguita.lib.platform.registry.ResourcefulRegistry;
import com.macuguita.lib.platform.registry.builtin.base.ItemLikeEntry;
import com.macuguita.lib.platform.registry.builtin.base.ItemLikeHolderEntryGuita;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class ResourcefulBlockRegistry implements ResourcefulRegistry<Block> {

    private final String namespace;
    private final ResourcefulRegistry<Block> registry;

    public ResourcefulBlockRegistry(String id) {
        this(ResourcefulRegistries.create(Registries.BLOCK, id));
    }

    public ResourcefulBlockRegistry(ResourcefulRegistry<Block> parent) {
        this.namespace = Objects.requireNonNull(parent.namespace(), "Parent registry must have a namespace.");
        this.registry = parent;
    }

    public <I extends Block> ItemLikeEntry<I> register(String id, Function<AbstractBlock.Settings, I> factory, Supplier<Block.Settings> getter) {
        RegistryKey<Block> key = RegistryKey.of(Registries.BLOCK.getKey(), Identifier.of(this.namespace, id));
        return this.register(id, () -> factory.apply(getter.get().registryKey(key)));
    }

    @Override
    public String namespace() {
        return this.namespace;
    }

    @Override
    public <I extends Block> ItemLikeEntry<I> register(String id, Supplier<I> supplier) {
        return new ItemLikeEntry<>(this.registry.register(id, supplier));
    }

    @Override
    public ItemLikeHolderEntryGuita<Block> registerHolder(String id, Supplier<Block> supplier) {
        return new ItemLikeHolderEntryGuita<>(this.registry.registerHolder(id, supplier));
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
