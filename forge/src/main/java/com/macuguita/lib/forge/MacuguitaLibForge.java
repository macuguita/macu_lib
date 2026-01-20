package com.macuguita.lib.forge;

import com.macuguita.lib.MacuguitaLib;
import com.macuguita.lib.supporters.RoleChecker;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("removal")
@Mod(MacuguitaLib.MOD_ID)
public final class MacuguitaLibForge {

    public MacuguitaLibForge() {
        MacuguitaLib.init();
    }

    @Mod.EventBusSubscriber(
            modid = MacuguitaLib.MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    static class MacuguitaLibEvents {

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            RoleChecker.shutdown();
        }
    }
}
