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

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import com.macuguita.lib.api.reg.GuitaHolderRegistryEntry;
import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;
import com.macuguita.lib.impl.reg.GuitaRegistryEntries;

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
		return entries.add(
			FabricGuitaRegistryEntry.of(
				this.registry, Identifier.fromNamespaceAndPath(this.id, id), supplier));
	}

	@Override
	public GuitaHolderRegistryEntry<T> registerForHolder(String id, Supplier<T> supplier) {
		return entries.add(
			FabricGuitaHolderRegistryEntry.of(
				this.registry, Identifier.fromNamespaceAndPath(this.id, id), supplier));
	}

	@Override
	public Collection<GuitaRegistryEntry<T>> getEntries() {
		return this.entries.getEntries();
	}

	@Override
	public void init() {}
}
