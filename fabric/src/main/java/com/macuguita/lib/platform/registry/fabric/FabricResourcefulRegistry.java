package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.RegistryEntries;
import com.macuguita.lib.platform.registry.RegistryEntryGuitaRegistryEntry;
import com.macuguita.lib.platform.registry.ResourcefulRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Supplier;

public class FabricResourcefulRegistry<T> implements ResourcefulRegistry<T> {

    private final RegistryEntries<T> entries = new RegistryEntries<>();
    private final Registry<T> registry;
    private final String id;

    public FabricResourcefulRegistry(Registry<T> registry, String id) {
        this.registry = registry;
        this.id = id;
    }

    @Override
    public String namespace() {
        return this.id;
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return entries.add(FabricRegistryEntry.of(this.registry, Identifier.of(this.id, id), supplier));
    }

    @Override
    public RegistryEntryGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        return entries.add(FabricHolderGuitaRegistryEntry.of(this.registry, Identifier.of(this.id, id), supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        // NO-OP
    }
}
