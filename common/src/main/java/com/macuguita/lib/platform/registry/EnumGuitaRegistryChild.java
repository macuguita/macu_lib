package com.macuguita.lib.platform.registry;

import java.util.EnumMap;
import java.util.function.Supplier;

public class EnumGuitaRegistryChild<E extends Enum<E>, T> extends GuitaRegistryChild<T> {

    private final EnumMap<E, GuitaRegistryEntries<T>> entries;

    public EnumGuitaRegistryChild(Class<E> enumClass, GuitaRegistry<T> parent) {
        super(parent);
        entries = new EnumMap<>(enumClass);
    }

    public <I extends T> GuitaRegistryEntry<I> register(E enumValue, String id, Supplier<I> supplier) {
        return entries.computeIfAbsent(enumValue, a -> new GuitaRegistryEntries<>())
            .add(super.register(id, supplier));
    }

    public GuitaRegistryEntries<T> getEntries(E enumValue) {
        return entries.get(enumValue);
    }

}