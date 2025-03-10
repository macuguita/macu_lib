package com.macuguita.lib.platform.registry.forge;


import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;

public class GuitaRegistriesImpl {
    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        return new ForgeGuitaRegistry<>(registry, id);
    }
}
