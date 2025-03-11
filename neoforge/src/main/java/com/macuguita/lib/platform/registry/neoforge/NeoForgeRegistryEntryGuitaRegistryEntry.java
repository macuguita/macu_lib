package com.macuguita.lib.platform.registry.neoforge;

import com.macuguita.lib.platform.registry.RegistryEntryGuitaRegistryEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeRegistryEntryGuitaRegistryEntry<R> implements RegistryEntryGuitaRegistryEntry<R> {

    private final DeferredHolder<R, R> object;

    public NeoForgeRegistryEntryGuitaRegistryEntry(DeferredHolder<R, R> object) {
        this.object = object;
    }

    @Override
    public RegistryEntry<R> registryEntry() {
        return object;
    }

    @Override
    public Identifier getId() {
        return object.getId();
    }
}
