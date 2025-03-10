package com.macuguita.lib.platform.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.item.ItemConvertible;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ItemConvertibleGuitaRegistry<T extends ItemConvertible> implements GuitaRegistry<T> {

    private final GuitaRegistry<T> parent;
    private final List<ItemConvertibleEntryGuita<T>> entries = new ArrayList<>();

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
    public HolderGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        HolderEntryGuita<T> entry = new HolderEntryGuita<>(parent.registerHolder(id, supplier));
        this.entries.add(entry);
        return entry;
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public Collection<ItemConvertibleEntryGuita<T>> getItemConvertibleEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    @Override
    public void init() {
        this.parent.init();
    }

    public interface ItemConvertibleEntryGuita<T extends ItemConvertible> extends GuitaRegistryEntry<T>, ItemConvertible {}

    public record EntryGuita<T extends ItemConvertible>(GuitaRegistryEntry<T> entry) implements ItemConvertibleEntryGuita<T> {

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

    public record HolderEntryGuita<T extends ItemConvertible>(
            HolderGuitaRegistryEntry<T> entry) implements HolderGuitaRegistryEntry<T>, ItemConvertibleEntryGuita<T> {

        @Override
        public RegistryEntry<T> holder() {
            return entry.holder();
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