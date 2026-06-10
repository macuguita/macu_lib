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

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;

/**
 * Represents a generic registry for storing and managing objects of type {@code T}.
 *
 * <p>Registries can hold entries that are lazily initialized via {@link Supplier} and can be
 * iterated over or streamed. This interface is designed to be used by mod frameworks and libraries
 * to organize game objects, items, or other data types.
 *
 * @param <T> The type of object stored in this registry.
 */
public interface GuitaRegistry<T> {

	/**
	 * Returns the namespace of this registry, if available.
	 *
	 * <p>This is typically used to identify the mod or library that owns the registry.
	 *
	 * @return The registry namespace, or {@code null} if not set.
	 */
	default @Nullable String namespace() {
		return null;
	}

	/**
	 * Registers a new entry in this registry.
	 *
	 * <p>The entry is associated with the given {@code id} and lazily supplied via the provided
	 * {@link Supplier}.
	 *
	 * @param id       The name for this entry (e.g., {@code "item_name"}).
	 * @param supplier A {@link Supplier} that provides the object when needed.
	 * @param <I>      The type of the entry, which must extend {@code T}.
	 * @return A {@link GuitaRegistryEntry} representing the newly registered entry.
	 */
	<I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier);

	/**
	 * Registers a new entry in this registry, returning a {@link GuitaHolderRegistryEntry}.
	 *
	 * <p>The entry is associated with the given {@code id} and lazily supplied via the provided
	 * {@link Supplier}.
	 *
	 * @param id       The name for this entry (e.g., {@code "item_name"}).
	 * @param supplier A {@link Supplier} that provides the object when needed.
	 * @return A {@link GuitaHolderRegistryEntry} representing the newly registered entry.
	 */
	GuitaHolderRegistryEntry<T> registerForHolder(String id, Supplier<T> supplier);

	/**
	 * Returns a {@link Collection} of all entries in this registry.
	 *
	 * @return A collection of {@link GuitaRegistryEntry} objects.
	 */
	Collection<GuitaRegistryEntry<T>> getEntries();

	/**
	 * Returns a {@link Stream} of all registry entries.
	 *
	 * <p>This is a convenient method for functional operations on registry entries.
	 *
	 * @return A {@link Stream} of {@link GuitaRegistryEntry} objects.
	 */
	default Stream<GuitaRegistryEntry<T>> stream() {
		return getEntries().stream();
	}

	/**
	 * Returns a {@link Stream} of the actual objects bound to the registry entries.
	 *
	 * <p>This is equivalent to mapping {@link GuitaRegistryEntry#get()} over the registry entries.
	 *
	 * @return A {@link Stream} of objects of type {@code T}.
	 */
	default Stream<T> boundStream() {
		return stream().map(GuitaRegistryEntry::get);
	}

	/**
	 * Initializes the registry.
	 *
	 * <p>This method should be called after all entries are registered, and will register these items
	 * into Minecraft.
	 */
	void init();
}
