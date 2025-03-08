package com.macuguita.lib.neoforge;

import com.macuguita.lib.MacuguitaLib;
import com.macuguita.lib.platform.neoforge.RegUtilsImpl;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(MacuguitaLib.MOD_ID)
public final class MacuguitaLibNeoForge {

    public MacuguitaLibNeoForge(IEventBus modEventBus) {

        // Run our common setup.
        MacuguitaLib.init();
        RegUtilsImpl.register(modEventBus);
    }
}
