package com.macuguita.lib.supporters;

import com.macuguita.lib.MacuguitaLib;
import net.minecraft.util.Identifier;

public enum Capes {

    DEVELOPER("developer", Identifier.of(MacuguitaLib.MOD_ID, "textures/capes/developer_cape.png"));

    private final String role;
    private final Identifier texture;

    Capes(String role, Identifier texture) {
        this.role = role;
        this.texture = texture;
    }

    public String getRole() {
        return role;
    }

    public Identifier getTexture() {
        return texture;
    }

    /**
     * Gets the cape texture for a specific role.
     */
    public static Identifier getCapeTextureForRole(String role) {
        for (Capes cape : Capes.values()) {
            if (cape.getRole().equals(role)) {
                return cape.getTexture();
            }
        }
        return null; // No cape for this role
    }
}
