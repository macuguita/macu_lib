package com.macuguita.lib.platform.registry;

import net.minecraft.registry.entry.RegistryEntry;

public interface HolderGuitaRegistryEntry<T> extends GuitaRegistryEntry<T> {

    RegistryEntry<T> holder();

    @Override
    default T get() {
        return holder().value();
    }

}