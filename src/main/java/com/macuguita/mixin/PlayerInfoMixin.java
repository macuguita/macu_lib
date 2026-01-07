package com.macuguita.mixin;

import com.macuguita.supporters.CapeManager;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

    @Inject(
            method = "getSkin",
            at = @At("RETURN"),
            order = 1001,
            cancellable = true
    )
    private void macu_lib$onGetSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        PlayerInfo player = (PlayerInfo) (Object) this;
        PlayerSkin originalSkin = cir.getReturnValue();

        if (originalSkin == null) return;

        UUID playerUUID = player.getProfile().id();

        if (!CapeManager.hasCape(playerUUID)) return;

        Identifier capeTexture = CapeManager.getPlayerCape(playerUUID);

        if (capeTexture == null) return;

        ClientAsset.ResourceTexture capeAsset = new ClientAsset.ResourceTexture(capeTexture);

        cir.setReturnValue(new PlayerSkin(
                originalSkin.body(),
                capeAsset,
                originalSkin.elytra(),
                originalSkin.model(),
                originalSkin.secure()
        ));
    }
}
