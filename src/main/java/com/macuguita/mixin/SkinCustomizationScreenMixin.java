package com.macuguita.mixin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.llamalad7.mixinextras.sugar.Local;
import com.macuguita.supporters.CapeManager;
import com.macuguita.supporters.SupporterData;
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
