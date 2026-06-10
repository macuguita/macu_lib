/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.platform.fabric.reg;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.macuguita.lib.api.reg.GuitaHolderRegistryEntry;

@ApiStatus.Internal
public class FabricGuitaHolderRegistryEntry<T> implements GuitaHolderRegistryEntry<T> {

	private final Identifier id;
	private final Holder<T> value;

	private FabricGuitaHolderRegistryEntry(Identifier id, Holder<T> value) {
		this.id = id;
		this.value = value;
	}

	public static <T, I extends T> FabricGuitaHolderRegistryEntry<I> of(
		Registry<T> registry, Identifier id, Supplier<I> supplier) {
		return new FabricGuitaHolderRegistryEntry<>(id, Registry.registerForHolder(registry, id, supplier.get()));
	}

	@Override
	public Holder<T> holder() {
		return this.value;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}
}
