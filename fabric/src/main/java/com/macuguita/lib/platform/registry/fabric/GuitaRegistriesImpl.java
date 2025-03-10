package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;

public class GuitaRegistriesImpl {
    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        return new FabricGuitaRegistry<>(registry, id);
    }
}
