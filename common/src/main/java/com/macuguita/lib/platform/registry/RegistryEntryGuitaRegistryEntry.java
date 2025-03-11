package com.macuguita.lib.platform.registry;

import net.minecraft.registry.entry.RegistryEntry;

public interface RegistryEntryGuitaRegistryEntry<T> extends GuitaRegistryEntry<T> {

    RegistryEntry<T> registryEntry();

    @Override
    default T get() {
        return registryEntry().value();
    }

}