package com.macuguita.lib.platform.registry.fabric;

import com.macuguita.lib.platform.registry.RegistryEntryGuitaRegistryEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class FabricRegistryEntryGuitaRegistryEntry<T> implements RegistryEntryGuitaRegistryEntry<T> {

    private final Identifier id;
    private final RegistryEntry<T> value;

    private FabricRegistryEntryGuitaRegistryEntry(Identifier id, RegistryEntry<T> value) {
        this.id = id;
        this.value = value;
    }

    public static <T, I extends T> FabricRegistryEntryGuitaRegistryEntry<T> of(Registry<T> registry, Identifier id, Supplier<I> supplier) {
        return new FabricRegistryEntryGuitaRegistryEntry<>(id, Registry.registerReference(registry, id, supplier.get()));
    }

    @Override
    public RegistryEntry<T> registryEntry() {
        return this.value;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }
}