/*
 * macu_lib
 * Copyright (C) 2026 macuguita
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

package com.macuguita.mixin;

import java.util.UUID;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.macuguita.supporters.CapeManager;
import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {

	@Shadow
	public abstract GameProfile getProfile();

	@ModifyReturnValue(
			method = "getSkin",
			at = @At("RETURN")
	)
	private PlayerSkin macu_lib$onGetSkin(PlayerSkin original) {
		UUID playerUUID = this.getProfile().id();

		if (!CapeManager.hasCape(playerUUID)) return original;

		Identifier capeTexture = CapeManager.getPlayerCape(playerUUID);

		if (capeTexture == null) return original;

		ClientAsset.ResourceTexture capeAsset = new ClientAsset.ResourceTexture(capeTexture);

		return new PlayerSkin(
				original.body(),
				capeAsset,
				original.elytra(),
				original.model(),
				original.secure()
		);
	}
}
