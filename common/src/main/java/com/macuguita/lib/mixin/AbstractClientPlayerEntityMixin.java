package com.macuguita.lib.mixin;

import com.macuguita.lib.supporters.Capes;
import com.macuguita.lib.supporters.RoleChecker;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @Inject(
            method = "getCapeTexture",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        String role = RoleChecker.getPlayerRole(player.getUuid());

        if (role != null) {
            Identifier capeTexture = Capes.getCapeTextureForRole(role);
            if (capeTexture != null) {
                cir.setReturnValue(capeTexture);
            }
        }
    }
}