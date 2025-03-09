package com.macuguita.lib.forge;

import com.macuguita.lib.MacuguitaLib;
import com.macuguita.lib.platform.forge.RegUtilsImpl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@SuppressWarnings("removal")
@Mod(MacuguitaLib.MOD_ID)
public final class MacuguitaLibForge {

    public MacuguitaLibForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Run our common setup.
        MacuguitaLib.init();
        RegUtilsImpl.register(modEventBus);
    }
}
