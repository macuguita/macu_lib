package com.macuguita.lib.impl.persista;

import com.macuguita.lib.network.NetworkManager;

import net.minecraft.client.Minecraft;

public class PersistaClient {

	public static void init() {
		NetworkManager.registerClientS2CHandler(S2CDataUpdatedPacket.TYPE, (pkt) -> S2CDataUpdatedPacket.handle(Minecraft.getInstance(), Minecraft.getInstance().player, pkt));
	}
}
