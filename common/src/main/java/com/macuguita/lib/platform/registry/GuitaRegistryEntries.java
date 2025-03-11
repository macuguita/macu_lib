package com.macuguita.lib.platform.registry;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class GuitaRegistryEntries<T> {

    private final List<GuitaRegistryEntry<T>> entries = new ArrayList<>();

    public <I extends T> GuitaRegistryEntry<I> add(GuitaRegistryEntry<I> entry) {
        //noinspection unchecked
        entries.add((GuitaRegistryEntry<T>) entry);
        return entry;
    }

    public List<GuitaRegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(entries);
    }

}