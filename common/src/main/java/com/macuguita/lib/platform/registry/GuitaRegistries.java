package com.macuguita.lib.platform.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.registry.Registry;
import org.apache.commons.lang3.NotImplementedException;

public class GuitaRegistries {

    public static <T> GuitaRegistry<T> create(GuitaRegistry<T> parent) {
        return new GuitaRegistryChild<>(parent);
    }

    @ExpectPlatform
    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <D, T extends GuitaRegistry<D>> T create(GuitaRegistryType<D, T> type, String id) {
        throw new NotImplementedException();
    }
}
