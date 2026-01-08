package com.macuguita.lib.neoforge.reg;

//? neoforge {

/*import java.util.Collection;
import java.util.function.Supplier;

import com.macuguita.lib.reg.GuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistryEntries;
import com.macuguita.lib.reg.GuitaRegistryEntry;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

@ApiStatus.Internal
public class NeoForgeGuitaRegistry<T> implements GuitaRegistry<T> {

	private final DeferredRegister<T> register;
	private final GuitaRegistryEntries<T> entries = new GuitaRegistryEntries<>();

	public NeoForgeGuitaRegistry(Registry<T> registry, String id) {
		this.register = DeferredRegister.create(registry.key(), id);
	}

	@Override
	public String namespace() {
		return this.register.getNamespace();
	}

	@Override
	public <I extends T> GuitaRegistryEntry<I> register(String id, Supplier<I> supplier) {
		return this.entries.add(new NeoForgeGuitaRegistryEntry<>(register.register(id, supplier)));
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
*///?}
