package com.macuguita.lib.fabric;

import com.macuguita.lib.MacuguitaLib;
import com.macuguita.lib.supporters.RoleChecker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class MacuguitaLibFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        MacuguitaLib.init();
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            RoleChecker.shutdown();
        });
    }

}
