package com.macuguita.lib.neoforge;

//? neoforge {

import com.macuguita.lib.MacuLib;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(MacuLib.MOD_ID)
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint(IEventBus modBus) {
        MacuLib.init();
    }

    @EventBusSubscriber(modid = MacuLib.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            MacuLib.LOGGER.info("Initializing {} Client", MacuLib.MOD_ID);
        }
    }

}
//?}