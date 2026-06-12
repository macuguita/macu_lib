package com.macuguita.lib.impl.creativetab;

import com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent;
import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

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
