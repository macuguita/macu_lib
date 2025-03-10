package com.macuguita.lib.platform.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * @deprecated Use {@link GuitaRegistries#createForItems(String)} or {@link GuitaRegistries#createForBlocks(String)}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "1.21.5")
public class ItemConvertibleGuitaRegistry<T extends ItemConvertible> implements GuitaRegistry<T> {

    private final GuitaRegistry<T> parent;
    private final List<ItemLikeEntryGuita<T>> entries = new ArrayList<>();

    public ItemConvertibleGuitaRegistry(Registry<T> registry, String id) {
        this.parent = GuitaRegistries.create(registry, id);
    }

    @Override
    public String namespace() {
        return this.parent.namespace();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <I extends T> EntryGuita<I> register(String id, Supplier<I> supplier) {
        EntryGuita<I> entry = new EntryGuita<>(parent.register(id, supplier));
        this.entries.add((EntryGuita<T>) entry);
        return entry;
    }

    @Override
    public RegistryEntryGuitaRegistryEntry<T> registerRegistryEntry(String id, Supplier<T> supplier) {
        RegistryEntryEntryGuita<T> entry = new RegistryEntryEntryGuita<>(parent.registerRegistryEntry(id, supplier));
        this.entries.add(entry);
        return entry;
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    public Collection<ItemLikeEntryGuita<T>> getItemLikeEntries() {
        return ImmutableList.copyOf(this.entries);
    }

    @Override
    public void init() {
        this.parent.init();
    }

    public interface ItemLikeEntryGuita<T extends ItemConvertible> extends GuitaRegistryEntry<T>, ItemConvertible {}

    public record EntryGuita<T extends ItemConvertible>(GuitaRegistryEntry<T> entry) implements ItemLikeEntryGuita<T> {

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

    public record RegistryEntryEntryGuita<T extends ItemConvertible>(
            RegistryEntryGuitaRegistryEntry<T> entry) implements RegistryEntryGuitaRegistryEntry<T>, ItemLikeEntryGuita<T> {

        @Override
        public RegistryEntry<T> registryEntry() {
            return entry.registryEntry();
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