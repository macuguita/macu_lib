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
package com.macuguita.lib.impl.creativetab;

import java.util.HashMap;
import java.util.Map;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent;

@ApiStatus.Internal
public final class ModifyCreativeTabOutputEvents {
	private static final Map<ResourceKey<CreativeModeTab>, Event<Identifier, ModifyCreativeTabOutputEvent>> EVENTS = new HashMap<>();

	private ModifyCreativeTabOutputEvents() {}

	public static Event<Identifier, ModifyCreativeTabOutputEvent> getOrCreate(ResourceKey<CreativeModeTab> key) {
		return EVENTS.computeIfAbsent(key, k -> YumiEvents.EVENTS.create(ModifyCreativeTabOutputEvent.class, listeners -> (tab, output) -> {
			for (var listener : listeners) {
				listener.modifyOutput(tab, output);
			}
		}));
	}

	@Nullable
	public static Event<Identifier, ModifyCreativeTabOutputEvent> get(ResourceKey<CreativeModeTab> key) {
		return EVENTS.get(key);
	}
}
