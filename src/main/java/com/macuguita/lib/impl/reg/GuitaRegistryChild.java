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
package com.macuguita.lib.impl.reg;

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import com.macuguita.lib.api.reg.GuitaHolderRegistryEntry;
import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;

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
	public GuitaHolderRegistryEntry<T> registerForHolder(String id, Supplier<T> supplier) {
		return this.entries.add(parent.registerForHolder(id, supplier));
	}

	@Override
	public Collection<GuitaRegistryEntry<T>> getEntries() {
		return entries.getEntries();
	}

	@Override
	public void init() {}
}
