package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryType;
import net.minecraft.registry.Registry;

public class GuitaRegistriesImpl {

    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        return new FabricGuitaRegistry<>(registry, id);
    }

    @SuppressWarnings("unchecked")
    public static <D, T extends GuitaRegistry<D>> T create(GuitaRegistryType<D, T> type, String id) {
        throw new IllegalArgumentException("Unknown registry type: " + type);
    }
}
