package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.ResourcefulRegistry;
import com.macuguita.lib.platform.registry.ResourcefulRegistryType;
import net.minecraft.registry.Registry;

public class ResourcefulRegistriesImpl {
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new FabricResourcefulRegistry<>(registry, id);
    }

    @SuppressWarnings("unchecked")
    public static <D, T extends ResourcefulRegistry<D>> T create(ResourcefulRegistryType<D, T> type, String id) {
        throw new IllegalArgumentException("Unknown registry type: " + type);
    }
}
