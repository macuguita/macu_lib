package com.macuguita.lib.mixin;

import com.macuguita.lib.supporters.Capes;
import com.macuguita.lib.supporters.RoleChecker;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @Inject(
            method = "getSkinTextures",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        SkinTextures originalTextures = cir.getReturnValue();

        if (originalTextures == null) return; // Avoid null reference issues

        String role = RoleChecker.getPlayerRole(player.getUuid());
        if (role != null) {
            Identifier capeTexture = Capes.getCapeTextureForRole(role);
            if (capeTexture != null) {
                // Create a new instance of SkinTextures with the custom cape texture
                cir.setReturnValue(new SkinTextures(
                        originalTextures.texture(),
                        originalTextures.textureUrl(),
                        capeTexture,  // Override cape texture
                        originalTextures.elytraTexture(),
                        originalTextures.model(),
                        originalTextures.secure()
                ));
            }
        }
    }
}
