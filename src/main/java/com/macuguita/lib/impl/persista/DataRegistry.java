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
package com.macuguita.lib.impl.persista;

import java.util.*;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

import com.mojang.serialization.Codec;

import com.macuguita.lib.api.persista.DataToken;

// Internal registry of all registered DataEntries
@ApiStatus.Internal
final class DataRegistry {

	private static final Map<ResourceLocation, DataEntry<?>> REGISTRY = new LinkedHashMap<>();

	private DataRegistry() {}

	static <T> DataToken<T> add(ResourceLocation id, Codec<T> codec) {
		if (REGISTRY.containsKey(id)) {
			throw new IllegalStateException("DataToken already registered for: " + id);
		}
		var entry = new DataEntry<>(id, codec);
		REGISTRY.put(id, entry);
		return entry;
	}

	static Collection<DataEntry<?>> values() {
		return Collections.unmodifiableCollection(REGISTRY.values());
	}

	static int size() {
		return REGISTRY.size();
	}

	static Optional<DataEntry<?>> getById(ResourceLocation id) {
		return Optional.ofNullable(REGISTRY.get(id));
	}
}
