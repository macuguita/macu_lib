package com.macuguita.lib.platform.registry.neoforge;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.HolderGuitaRegistryEntry;
import com.macuguita.lib.platform.registry.RegistryEntries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class NeoForgeGuitaRegistry<T> implements GuitaRegistry<T> {

    private final DeferredRegister<T> register;
    private final RegistryEntries<T> entries = new RegistryEntries<>();

    public NeoForgeGuitaRegistry(Registry<T> registry, String id) {
        this.register = DeferredRegister.create(registry.getKey(), id);
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new NeoForgeRegistryEntry<>(register.register(id, supplier)));
    }

    @Override
    public HolderGuitaRegistryEntry<T> registerHolder(String id, Supplier<T> supplier) {
        return this.entries.add(new NeoForgeHolderGuitaRegistryEntry<>(register.register(id, supplier)));
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