package com.macuguita.lib.reg;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

@ApiStatus.NonExtendable
public interface GuitaRegistry<T> {

	default @Nullable String namespace() {
		return null;
	}

	<I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier);

	Collection<GuitaRegistryEntry<T>> getEntries();

	default Stream<GuitaRegistryEntry<T>> stream() {
		return getEntries().stream();
	}

	default Stream<T> boundStream() {
		return stream().map(GuitaRegistryEntry::get);
	}

	void init();
}
