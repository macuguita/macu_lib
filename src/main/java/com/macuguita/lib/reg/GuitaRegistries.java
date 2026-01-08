/*
 * macu_lib
 * Copyright (C) 2026 macuguita
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

package com.macuguita.lib.reg;

import com.macuguita.lib.Platform;

import net.minecraft.core.Registry;

/**
 * Utility class for creating and managing {@link GuitaRegistry} instances.
 * <p>
 * Provides methods to create standard or child registries, making it easier
 * to group registry entries and integrate with Minecraft registries.
 */
public class GuitaRegistries {

	/**
	 * Creates a **child registry** of the given parent {@link GuitaRegistry}.
	 * <p>
	 * Entries added to this child registry are automatically added to the parent,
	 * which is useful for logically grouping registry entries without duplicating code.
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
	 * <p>
	 * This allows mod-specific registries to be created that integrate with
	 * Minecraft's built-in registries while keeping entries namespaced to the mod.
	 *
	 * @param registry The Minecraft {@link Registry} to wrap, e.g., from
	 *                 {@link net.minecraft.core.registries.BuiltInRegistries}.
	 * @param id       The namespace of the mod creating this registry.
	 * @param <T>      The type of entries stored in the registry.
	 * @return A new {@link GuitaRegistry} instance.
	 */
	public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
		return Platform.INSTANCE.createGuitaRegistry(registry, id);
	}
}
