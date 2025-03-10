package com.macuguita.lib.platform.registry;

public final class GuitaRegistryType<D, T extends GuitaRegistry<D>> {

    private final Class<T> type;

    private GuitaRegistryType(Class<T> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GuitaRegistryType{type=" + type + "}";
    }
}
