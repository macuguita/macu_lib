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

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.ApiStatus;

import com.macuguita.lib.api.reg.GuitaRegistryEntry;

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
