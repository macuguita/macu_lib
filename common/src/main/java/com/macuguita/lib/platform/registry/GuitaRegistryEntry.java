package com.macuguita.lib.platform.registry;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public interface GuitaRegistryEntry<T> extends Supplier<T> {

    @Override
    T get();

    Identifier getId();

}