package com.macuguita.lib.platform.registry.neoforge;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.RegistryEntryGuitaRegistryEntry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class NeoForgeGuitaRegistry<T> implements GuitaRegistry<T> {

    private final DeferredRegister<T> register;
    private final GuitaRegistryEntries<T> entries = new GuitaRegistryEntries<>();

    public NeoForgeGuitaRegistry(Registry<T> registry, String id) {
        this.register = DeferredRegister.create(registry.getKey(), id);
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new NeoForgeRegistryEntry<>(register.register(id, supplier)));
    }

    @Override
    public RegistryEntryGuitaRegistryEntry<T> registerRegistryEntry(String id, Supplier<T> supplier) {
        return this.entries.add(new NeoForgeRegistryEntryGuitaRegistryEntry<>(register.register(id, supplier)));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        register.register(ModLoadingContext.get().getActiveContainer().getEventBus());
    }
}