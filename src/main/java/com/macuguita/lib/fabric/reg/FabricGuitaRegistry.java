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

package com.macuguita.lib.fabric.reg;

//? fabric {

import java.util.Collection;
import java.util.function.Supplier;

import com.macuguita.lib.reg.GuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistryEntries;
import com.macuguita.lib.reg.GuitaRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

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
		return entries.add(FabricGuitaRegistryEntry.of(this.registry, Identifier.fromNamespaceAndPath(this.id, id), supplier));
	}

	@Override
	public Collection<GuitaRegistryEntry<T>> getEntries() {
		return this.entries.getEntries();
	}

	@Override
	public void init() {
	}
}
//?}
