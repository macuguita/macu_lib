package com.macuguita.lib.impl.persista;

import com.macuguita.lib.api.network.PacketRegistry;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;

public class PersistaClient implements ClientModInitializer {

	@Override
	public void onInitializeClient(ModContainer mod) {
		PacketRegistry.registerClientboundPacketHandler(ClientboundDataUpdatedPacket.TYPE, ClientboundDataUpdatedPacket::handle);
	}
}
