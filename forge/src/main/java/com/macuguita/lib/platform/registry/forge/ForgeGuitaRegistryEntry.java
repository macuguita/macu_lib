package com.macuguita.lib.platform.registry.forge;

import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraftforge.registries.RegistryObject;

public class ForgeGuitaRegistryEntry<T> implements GuitaRegistryEntry<T> {

    private final RegistryObject<T> object;

    public ForgeGuitaRegistryEntry(RegistryObject<T> object) {
        this.object = object;
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public Identifier getId() {
        return object.getId();
    }
}
