package com.macuguita.lib.platform.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemConvertibleGuitaRegistry<T extends ItemConvertible> implements GuitaRegistry<T> {

    private final GuitaRegistry<T> parent;
    private final List<EntryGuita<T>> entries = new ArrayList<>();

    public ItemConvertibleGuitaRegistry(Registry<T> registry, String id) {
        this.parent = GuitaRegistries.create(registry, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> EntryGuita<I> register(String id, Supplier<I> supplier) {
        EntryGuita<I> entry = new EntryGuita<>(parent.register(id, supplier));
        this.entries.add((EntryGuita<T>) entry);
        return entry;
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public Collection<EntryGuita<T>> getItemConvertibleEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    @Override
    public void init() {
        this.parent.init();
    }

    public record EntryGuita<T extends ItemConvertible>(GuitaRegistryEntry<T> entry) implements GuitaRegistryEntry<T>, ItemConvertible {

        @Override
        public T get() {
            return entry.get();
        }

        @Override
        public Identifier getId() {
            return entry.getId();
        }

        @Override
        public @NotNull Item asItem() {
            return entry.get().asItem();
        }
    }
}