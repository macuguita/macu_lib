package com.macuguita.lib.neoforge.reg;

//? neoforge {
/*import com.macuguita.lib.reg.GuitaRegistryEntry;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class NeoForgeGuitaRegistryEntry<R, T extends R> implements GuitaRegistryEntry<T> {

    private final DeferredHolder<R, T> object;

    public NeoForgeGuitaRegistryEntry(DeferredHolder<R, T> object) {
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
*///?}