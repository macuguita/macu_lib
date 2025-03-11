package com.macuguita.lib.platform.registry.forge;

import com.macuguita.lib.platform.registry.GuitaRegistryEntries;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import net.minecraft.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

public class ForgeGuitaRegistry<T> implements GuitaRegistry<T> {

    private final DeferredRegister<T> register;
    private final GuitaRegistryEntries<T> entries = new GuitaRegistryEntries<>();

    public ForgeGuitaRegistry(Registry<T> registry, String id) {
        this.register = DeferredRegister.create(registry.getKey(), id);
    }

    @Override
    public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
        return this.entries.add(new ForgeGuitaRegistryEntry<>(register.register(id, supplier)));
    }

    @Override
    public Collection<GuitaRegistryEntry<T>> getEntries() {
        return this.entries.getEntries();
    }

    @Override
    public void init() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        register.register(bus);
    }
}