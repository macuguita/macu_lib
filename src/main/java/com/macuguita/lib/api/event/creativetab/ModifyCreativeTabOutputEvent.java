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
package com.macuguita.lib.api.event.creativetab;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.macuguita.lib.api.creativetab.GuitaCreativeModeTabOutput;
import com.macuguita.lib.impl.creativetab.ModifyCreativeTabOutputEvents;

public interface ModifyCreativeTabOutputEvent {

	/**
	 * This event allows the output of any creative mode tab to be modified.
	 *
	 * <p>Use an {@code if} statement if you only want to affect one creative mod tab.
	 *
	 * <p>This event is invoked after those two more specific events.
	 */
	Event<Identifier, ModifyCreativeTabOutputEvent> EVENT = YumiEvents.EVENTS.create(ModifyCreativeTabOutputEvent.class, listeners -> (tab, output) -> {
		for (var listener : listeners) {
			listener.modifyOutput(tab, output);
		}
	});

	/**
	 * Returns the modify output event for a specific creative mode tab, identified by its
	 * {@link ResourceKey}. Prefer this over {@link #EVENT} when targeting a single tab.
	 *
	 * @param tabKey the resource key of the creative mode tab to modify
	 * @return the event for that specific tab
	 */
	static Event<Identifier, ModifyCreativeTabOutputEvent> forTab(ResourceKey<CreativeModeTab> tabKey) {
		return ModifyCreativeTabOutputEvents.getOrCreate(tabKey);
	}

	void modifyOutput(CreativeModeTab tab, GuitaCreativeModeTabOutput output);
}
