package com.macuguita.lib.fabric.reg;

//? fabric {
import com.macuguita.lib.reg.GuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistryEntries;
import com.macuguita.lib.reg.GuitaRegistryEntry;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.function.Supplier;

public class FabricGuitaRegistry<T> implements GuitaRegistry<T> {

    private final GuitaRegistryEntries<T> entries = new GuitaRegistryEntries<>();
    private final Registry<T> registry;
    private final String id;

    public FabricGuitaRegistry(Registry<T> registry, String id) {
        this.registry = registry;
        this.id = id;
    }

    @Override
    public String namespace() {
        return this.id;
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return entries.add(FabricGuitaRegistryEntry.of(this.registry, Identifier.fromNamespaceAndPath(this.id, id), supplier));
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
//?}
