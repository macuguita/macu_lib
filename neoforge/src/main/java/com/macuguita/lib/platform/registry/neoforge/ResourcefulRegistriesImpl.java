package com.macuguita.lib.platform.registry.neoforge;

import com.macuguita.lib.platform.registry.ResourcefulRegistry;
import com.macuguita.lib.platform.registry.ResourcefulRegistryType;
import net.minecraft.registry.Registry;

public class ResourcefulRegistriesImpl {
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new NeoForgeResourcefulRegistry<>(registry, id);
    }

    @SuppressWarnings("unchecked")
    public static <D, T extends ResourcefulRegistry<D>> T create(ResourcefulRegistryType<D, T> type, String id) {
        throw new IllegalArgumentException("Unknown registry type: " + type);
    }
}
