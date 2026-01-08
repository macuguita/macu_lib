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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus;

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
