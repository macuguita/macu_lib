package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.HolderGuitaRegistryEntry;
import com.macuguita.lib.platform.registry.RegistryEntries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Collection;
import java.util.function.Supplier;

public class FabricGuitaRegistry<T> implements GuitaRegistry<T> {

    private final RegistryEntries<T> entries = new RegistryEntries<>();
    private final Registry<T> registry;
    private final String id;

    public FabricGuitaRegistry(Registry<T> registry, String id) {
        this.registry = registry;
        this.id = id;
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return entries.add(FabricRegistryEntry.of(this.registry, Identifier.of(this.id, id), supplier));
    }

    @Override
    public HolderGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        return entries.add(FabricHolderRegistryEntry.of(this.registry, Identifier.of(this.id, id), supplier));
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
