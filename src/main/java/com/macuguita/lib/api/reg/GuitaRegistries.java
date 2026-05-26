/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.api.reg;

import net.minecraft.core.Registry;

import com.macuguita.lib.impl.platform.CommonAbstraction;
import com.macuguita.lib.impl.reg.GuitaRegistryChild;

/**
 * Utility class for creating and managing {@link GuitaRegistry} instances.
 *
 * <p>Provides methods to create standard or child registries, making it easier to group registry
 * entries and integrate with Minecraft's built-in registries.
 *
 * <h2>Implementation note</h2>
 *
 * <p>On NeoForge, Yumi's {@code Registry.register} bypass allows skipping deferred registries in
 * favor of vanilla's {@link net.minecraft.core.Registry#register} directly. However, to minimize
 * platform-specific "hacks" and maintain cross-platform compatibility, this class wraps
 * registration behind {@link CommonAbstraction}, letting each platform handle the details
 * internally while exposing a single consistent API. Because of this you <strong>CANNOT</strong> use this
 * from a mod initializer provided by Yumi on Neoforge, in the future I might add another
 * implementation that allows this.
 */
public final class GuitaRegistries {

    private GuitaRegistries() {}

    /**
     * Creates a <strong>child registry</strong> of the given parent {@link GuitaRegistry}.
     *
     * <p>Entries added to this child registry are automatically added to the parent, which is useful
     * for logically grouping registry entries without duplicating code.
     *
     * @param parent The parent {@link GuitaRegistry} to which this child will belong.
     * @param <T>    The type of entries stored in the registry.
     * @return A new {@link GuitaRegistry} instance representing the child registry.
     */
    public static <T> GuitaRegistry<T> create(GuitaRegistry<T> parent) {
        return new GuitaRegistryChild<>(parent);
    }

    /**
     * Creates a new {@link GuitaRegistry} for a given Minecraft {@link Registry}.
     *
     * <p>This allows mod-specific registries to be created that integrate with Minecraft's built-in
     * registries while keeping entries namespaced to the mod.
     *
     * @param registry The Minecraft {@link Registry} to wrap, e.g., from {@link
     *                 net.minecraft.core.registries.BuiltInRegistries}.
     * @param id       The namespace of the mod creating this registry.
     * @param <T>      The type of entries stored in the registry.
     * @return A new {@link GuitaRegistry} instance.
     */
    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        return CommonAbstraction.get().createGuitaRegistry(registry, id);
    }
}
