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
package com.macuguita.lib.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.llamalad7.mixinextras.sugar.Local;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.SkinCustomizationScreen;
import net.minecraft.network.chat.Component;

import com.macuguita.lib.impl.supporters.CapeManager;
import com.macuguita.lib.impl.supporters.SupporterData;

@Mixin(SkinCustomizationScreen.class)
public class SkinCustomizationScreenMixin {

	@Inject(
		method = "addOptions",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/OptionsList;addSmall(Ljava/util/List;)V", shift = At.Shift.BEFORE)
	)
	private void macu_lib$onAddOptions(CallbackInfo ci, @Local(ordinal = 0) List<AbstractWidget> widgets) {
		UUID localPlayer = Minecraft.getInstance().getUser().getProfileId();

		if (!CapeManager.isSupporter(localPlayer)) return;

		String current = CapeManager.SUPPORTER_DATA
			.getOrDefault(localPlayer, SupporterData.EMPTY)
			.selectedCape();

		var button = Button.builder(
			Component.literal("Cape: " + macu_lib$capeDisplayName(current)),
			btn -> {
				String next = macu_lib$cycleNext(localPlayer);
				btn.setMessage(Component.literal("Cape: " + macu_lib$capeDisplayName(next)));
			}
		).build();

		widgets.add(button);
	}

	@Unique
	private static List<@Nullable String> macu_lib$buildCapeList() {
		var list = new ArrayList<@Nullable String>();
		list.add(null);
		list.addAll(CapeManager.getAvailableCapes());
		return list;
	}

	@Unique
	private static String macu_lib$cycleNext(UUID playerId) {
		String current = CapeManager.SUPPORTER_DATA
			.getOrDefault(playerId, SupporterData.EMPTY)
			.selectedCape();
		var capes = macu_lib$buildCapeList();
		int index = capes.indexOf(current);
		String next = capes.get((index + 1) % capes.size());
		CapeManager.setSelectedCape(next);
		return next;
	}

	@Unique
	private static String macu_lib$capeDisplayName(@Nullable String cape) {
		return cape == null ? "None" : cape.substring(0, 1).toUpperCase() + cape.substring(1);
	}
}
