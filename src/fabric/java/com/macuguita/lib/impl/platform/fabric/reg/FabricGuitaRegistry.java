/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform.fabric.reg;

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;
import com.macuguita.lib.impl.reg.GuitaRegistryEntries;

@ApiStatus.Internal
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
        return entries.add(
            FabricGuitaRegistryEntry.of(
                this.registry, Identifier.fromNamespaceAndPath(this.id, id), supplier));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {}
}
