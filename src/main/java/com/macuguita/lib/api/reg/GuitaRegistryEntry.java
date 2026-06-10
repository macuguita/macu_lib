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
package com.macuguita.lib.api.reg;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;

/**
 * Represents a single entry in a {@link GuitaRegistry}.
 *
 * <p>Each entry wraps an object of type {@code T} and provides a unique {@link Identifier}.
 * Registry entries are lazily initialized via {@link #get()} and can be used in streams or added to
 * other registries.
 *
 * @param <T> The type of object stored in this registry entry.
 */
public interface GuitaRegistryEntry<T> extends Supplier<T> {

	/**
	 * Returns the object stored in this registry entry.
	 *
	 * <p>This may trigger lazy initialization depending on the implementation.
	 *
	 * @return The object of type {@code T} contained in this entry.
	 */
	@Override
	T get();

	/**
	 * Returns the unique identifier for this registry entry.
	 *
	 * <p>The identifier is typically namespaced (e.g., {@code "modid:item_name"}) and corresponds to
	 * the ID used when registering the entry.
	 *
	 * @return The {@link Identifier} for this entry.
	 */
	Identifier getId();
}
