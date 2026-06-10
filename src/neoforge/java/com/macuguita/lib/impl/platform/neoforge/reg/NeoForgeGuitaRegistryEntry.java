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

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.Identifier;

import net.neoforged.neoforge.registries.DeferredHolder;

import com.macuguita.lib.api.reg.GuitaRegistryEntry;

@ApiStatus.Internal
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
