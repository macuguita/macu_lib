/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.reg;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;

/**
 * Represents a single entry in a {@link GuitaRegistry}.
 *
 * <p>Each entry wraps an object of type {@code T} and provides a unique {@link Identifier}.
 * Registry entries are lazily initialized via {@link #get()} and can be used in streams or added to
 * other registries.
 *
 * @param <T> The type of object stored in this registry entry.
 */
public interface GuitaRegistryEntry<T> extends Supplier<T> {

    /**
     * Returns the object stored in this registry entry.
     *
     * <p>This may trigger lazy initialization depending on the implementation.
     *
     * @return The object of type {@code T} contained in this entry.
     */
    @Override
    T get();

    /**
     * Returns the unique identifier for this registry entry.
     *
     * <p>The identifier is typically namespaced (e.g., {@code "modid:item_name"}) and corresponds to
     * the ID used when registering the entry.
     *
     * @return The {@link Identifier} for this entry.
     */
    Identifier getId();
}
