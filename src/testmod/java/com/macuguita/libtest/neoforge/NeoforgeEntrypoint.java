package com.macuguita.libtest.neoforge;

//? neoforge {

/*import com.macuguita.libtest.TestMod;
import com.macuguita.libtest.client.TestModClient;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(TestMod.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint() {
		TestMod.init();
	}

	@EventBusSubscriber(modid = TestMod.MOD_ID, value = Dist.CLIENT)
	public static class ClientEvents {
		@SubscribeEvent
		public static void onClientSetup(final FMLClientSetupEvent event) {
			TestModClient.init();
		}
	}
}
*///?}
