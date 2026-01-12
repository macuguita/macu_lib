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

/*import java.util.function.Supplier;

import com.macuguita.lib.reg.GuitaRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

@ApiStatus.Internal
public class FabricGuitaRegistryEntry<T> implements GuitaRegistryEntry<T> {

	private final Identifier id;
	private final T value;

	private FabricGuitaRegistryEntry(Identifier id, T value) {
		this.id = id;
		this.value = value;
	}

	public static <T, I extends T> FabricGuitaRegistryEntry<I> of(Registry<T> registry, Identifier id, Supplier<I> supplier) {
		return new FabricGuitaRegistryEntry<>(id, Registry.register(registry, id, supplier.get()));
	}

	@Override
	public T get() {
		return this.value;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}
}
*///?}
