package com.macuguita.lib.fabric;

//? fabric {
import com.macuguita.lib.MacuLib;
import net.fabricmc.api.ClientModInitializer;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MacuLib.LOG.info("Initializing {} Client", MacuLib.MOD_ID);
    }

}
//?}