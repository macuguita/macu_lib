package com.macuguita.lib.mixin;

import com.macuguita.lib.supporters.Capes;
import com.macuguita.lib.supporters.RoleChecker;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

    @Inject(
            method = "getSkinTextures",
            at = @At("TAIL"),
            order = 1001,
            cancellable = true
    )
    private void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        PlayerListEntry player = (PlayerListEntry) (Object) this;
        SkinTextures originalTextures = cir.getReturnValue();

        if (originalTextures == null) return;

        String role = RoleChecker.getPlayerRole(player.getProfile().id());
        if (role != null) {
            Identifier capeTexture = Capes.getCapeTextureForRole(role);
            if (capeTexture != null) {
                AssetInfo.TextureAssetInfo capeAsset = new AssetInfo.TextureAssetInfo(capeTexture);

                cir.setReturnValue(new SkinTextures(
                        originalTextures.body(),
                        capeAsset,
                        originalTextures.elytra(),
                        originalTextures.model(),
                        originalTextures.secure()
                ));
            }
        }
    }
}
