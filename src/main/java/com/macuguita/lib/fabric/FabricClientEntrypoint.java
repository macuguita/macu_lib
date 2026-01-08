package com.macuguita.lib.fabric;

//? fabric {

import com.macuguita.lib.MacuLib;

import net.fabricmc.api.ClientModInitializer;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		MacuLib.LOGGER.info("Initializing {} Client", MacuLib.MOD_ID);
	}

}
//?}
