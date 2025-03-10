package com.macuguita.lib.fabric;

import com.macuguita.lib.MacuguitaLib;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry;

public final class MacuguitaLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        // Run our common setup.
        MacuguitaLib.init();
    }

}
