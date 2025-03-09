package com.macuguita.lib;

import com.macuguita.lib.supporters.RoleChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MacuguitaLib {

    public static final String MOD_ID = "macu_lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static String modId;

    /**
     * Called by the depending mod to set its mod ID.
     */
    public static void setModId(String modId) {
        if (MacuguitaLib.modId != null) {
            throw new IllegalStateException("Mod ID has already been set!");
        }
        MacuguitaLib.modId = modId;
    }

    /**
     * Gets the mod ID of the depending mod.
     */
    public static String getModId() {
        if (modId == null) {
            throw new IllegalStateException("Mod ID has not been set! Call setModId() first.");
        }
        return modId;
    }

    public static void init() {
        setModId(MOD_ID);
        RoleChecker.init();
    }

}
