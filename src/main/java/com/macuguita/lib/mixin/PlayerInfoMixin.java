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

import java.util.UUID;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;

import com.mojang.authlib.GameProfile;

import com.macuguita.lib.impl.supporters.CapeManager;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {

	@Shadow
	public abstract GameProfile getProfile();

	@ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
	private PlayerSkin macu_lib$onGetSkin(PlayerSkin original) {
		var playerUUID = this.getProfile().id();

		if (!CapeManager.hasCape(playerUUID)) return original;

		Identifier capeTexture = CapeManager.getPlayerCape(playerUUID);

		if (capeTexture == null) return original;

		var capeAsset = new ClientAsset.ResourceTexture(capeTexture);

		return new PlayerSkin(original.body(), capeAsset, original.elytra(), original.model(), original.secure());
	}
}
