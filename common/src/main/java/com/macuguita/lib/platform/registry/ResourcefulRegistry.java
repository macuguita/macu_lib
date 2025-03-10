package com.macuguita.lib.platform.registry;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ResourcefulRegistry<T> {

    default String namespace() {
        return null;
    }

    <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier);

    RegistryEntryGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier);

    Collection<GuitaRegistryEntry<T>> getEntries();

    default Stream<GuitaRegistryEntry<T>> stream() {
        return getEntries().stream();
    }

    default Stream<T> boundStream() {
        return stream().map(GuitaRegistryEntry::get);
    }

    void init();
}
