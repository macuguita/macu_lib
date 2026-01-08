package com.macuguita.lib.fabric;

//? fabric {

import com.macuguita.lib.MacuLib;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FabricEntrypoint implements ModInitializer {

    @Override
    public void onInitialize() {
        MacuLib.init();
    }

}
//?}
