package com.macuguita.lib.reg;

import java.util.Collection;
import java.util.function.Supplier;

import org.jspecify.annotations.Nullable;

public class GuitaRegistryChild<T> implements GuitaRegistry<T> {

	private final GuitaRegistry<T> parent;
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
	public Collection<GuitaRegistryEntry<T>> getEntries() {
		return entries.getEntries();
	}

	@Override
	public void init() {
		//NO-OP
	}
}
