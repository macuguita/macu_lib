/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.reg;

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;

@ApiStatus.Internal
public class GuitaRegistryChild<T> implements GuitaRegistry<T> {
    // Backing parent registry
    private final GuitaRegistry<T> parent;
    // Entries registered in this child
    private final GuitaRegistryEntries<T> entries = new GuitaRegistryEntries<>();

    public GuitaRegistryChild(GuitaRegistry<T> parent) {
        this.parent = parent;
    }

    @Override
    public @Nullable String namespace() {
        return this.parent.namespace();
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(parent.register(id, supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return entries.getEntries();
    }

    @Override
    public void init() {}
}
