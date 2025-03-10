package com.macuguita.lib.forge;

import com.macuguita.lib.MacuguitaLib;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("removal")
@Mod(MacuguitaLib.MOD_ID)
public final class MacuguitaLibForge {

    public MacuguitaLibForge() {
        MacuguitaLib.init();
    }
}
