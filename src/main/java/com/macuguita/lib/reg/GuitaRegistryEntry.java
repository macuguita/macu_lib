package com.macuguita.lib.reg;

import java.util.function.Supplier;

import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface GuitaRegistryEntry<T> extends Supplier<T> {

	@Override
	T get();

	Identifier getId();

}
