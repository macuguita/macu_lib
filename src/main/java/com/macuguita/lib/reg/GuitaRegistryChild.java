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

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

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
	public void init() {
	}
}
