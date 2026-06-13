package com.macuguita.lib.client;

import com.macuguita.lib.impl.persista.S2CDataUpdatedPacket;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.supporters.CapeManager;

import net.minecraft.client.Minecraft;

public class MacuLibClient {

	public static void init() {
		CapeManager.fetchAvailableCapes();
		NetworkManager.registerClientS2CHandler(S2CDataUpdatedPacket.TYPE, (pkt) -> S2CDataUpdatedPacket.handle(Minecraft.getInstance(), Minecraft.getInstance().player, pkt));
	}
}
