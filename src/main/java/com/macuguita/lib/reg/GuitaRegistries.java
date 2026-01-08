package com.macuguita.lib.reg;

import com.macuguita.lib.Platform;

import net.minecraft.core.Registry;

public class GuitaRegistries {

	public static <T> GuitaRegistry<T> create(GuitaRegistry<T> parent) {
		return new GuitaRegistryChild<>(parent);
	}

	public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
		return Platform.INSTANCE.createGuitaRegistry(registry, id);
	}
}
