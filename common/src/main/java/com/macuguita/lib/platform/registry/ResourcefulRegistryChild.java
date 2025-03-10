package com.macuguita.lib.platform.registry;

import java.util.Collection;
import java.util.function.Supplier;

public class ResourcefulRegistryChild<T> implements ResourcefulRegistry<T> {

    private final ResourcefulRegistry<T> parent;
    private final RegistryEntries<T> entries = new RegistryEntries<>();

    public ResourcefulRegistryChild(ResourcefulRegistry<T> parent) {
        this.parent = parent;
    }

    @Override
    public String namespace() {
        return this.parent.namespace();
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(parent.register(id, supplier));
    }

    @Override
    public RegistryEntryGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        return this.entries.add(parent.registerHolder(id, supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {
        //NO-OP
    }
}