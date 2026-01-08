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
