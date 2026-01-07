package com.macuguita.lib.reg;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class GuitaRegistryEntries<T> {

    private final List<GuitaRegistryEntry<T>> entries = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <I extends T, E extends GuitaRegistryEntry<I>> E add(E entry) {
        entries.add((GuitaRegistryEntry<T>) entry);
        return entry;
    }

    public List<GuitaRegistryEntry<T>> getEntries() {
        return ImmutableList.copyOf(entries);
    }

}
