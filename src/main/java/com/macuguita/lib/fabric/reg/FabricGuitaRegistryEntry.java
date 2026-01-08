package com.macuguita.lib.fabric.reg;

//? fabric {

import java.util.function.Supplier;

import com.macuguita.lib.reg.GuitaRegistryEntry;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.ApiStatus;

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
//?}
