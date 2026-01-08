package com.macuguita.lib.neoforge.network;

//? neoforge {

/*import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@ApiStatus.Internal
@EventBusSubscriber(modid = MacuLib.MOD_ID, value = Dist.CLIENT)
public final class NeoForgeClientNetworkBootstrap {

	@SubscribeEvent
	public static void registerClientHandlers(RegisterClientPayloadHandlersEvent event) {
		for (NetworkManager.S2CRegistration<?> reg : NeoForgeNetworkBootstrap.S2C) {
			registerS2CHandler(event, reg);
		}
	}

	private static <T extends CustomPacketPayload> void registerS2CHandler(
			RegisterClientPayloadHandlersEvent event,
			NetworkManager.S2CRegistration<T> reg
	) {
		event.register(reg.type(), (payload, context) -> {
			// Handler supplier is called on client side only
			var handler = reg.handlerSupplier().get();
			handler.accept(payload);
		});
	}
}
*///?}
