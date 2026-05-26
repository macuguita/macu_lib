/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.platform.neoforge.reg;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import net.neoforged.neoforge.registries.DeferredHolder;

import com.macuguita.lib.api.reg.GuitaRegistryEntry;

@ApiStatus.Internal
public class NeoForgeGuitaRegistryEntry<R, T extends R> implements GuitaRegistryEntry<T> {

    private final DeferredHolder<R, T> object;

    public NeoForgeGuitaRegistryEntry(DeferredHolder<R, T> object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public Identifier getId() {
        return object.getId();
    }
}
