/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.reg;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus;

import com.macuguita.lib.api.reg.GuitaRegistryEntry;

@ApiStatus.Internal
public class GuitaRegistryEntries<T> {

    // Internal list of registry entries
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
