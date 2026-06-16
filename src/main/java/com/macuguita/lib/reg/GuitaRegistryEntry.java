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

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;

/**
 * Represents a single entry in a {@link GuitaRegistry}.
 * <p>
 * Each entry wraps an object of type {@code T} and provides a unique {@link ResourceLocation}.
 * Registry entries are lazily initialized via {@link #get()} and can be used in streams
 * or added to other registries.
 *
 * @param <T> The type of object stored in this registry entry.
 */
@ApiStatus.NonExtendable
public interface GuitaRegistryEntry<T> extends Supplier<T> {

	/**
	 * Returns the object stored in this registry entry.
	 * <p>
	 * This may trigger lazy initialization depending on the implementation.
	 *
	 * @return The object of type {@code T} contained in this entry.
	 */
	@Override
	T get();

	/**
	 * Returns the unique identifier for this registry entry.
	 * <p>
	 * The identifier is typically namespaced (e.g., {@code "modid:item_name"}) and
	 * corresponds to the ID used when registering the entry.
	 *
	 * @return The {@link ResourceLocation} for this entry.
	 */
	ResourceLocation getId();
}
