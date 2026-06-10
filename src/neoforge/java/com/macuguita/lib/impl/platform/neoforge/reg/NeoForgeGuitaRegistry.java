/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.platform.neoforge.reg;

import java.util.Collection;
import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.macuguita.lib.api.reg.GuitaRegistry;
import com.macuguita.lib.api.reg.GuitaRegistryEntry;
import com.macuguita.lib.impl.reg.GuitaRegistryEntries;

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
