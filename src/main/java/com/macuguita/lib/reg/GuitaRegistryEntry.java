package com.macuguita.lib.reg;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public interface GuitaRegistryEntry<T> extends Supplier<T> {

    @Override
    T get();

    Identifier getId();

}
