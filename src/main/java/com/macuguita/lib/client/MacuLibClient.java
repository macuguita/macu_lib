package com.macuguita.lib.client;

import com.macuguita.lib.impl.persista.Persista;
import com.macuguita.lib.impl.persista.PersistaClient;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.supporters.CapeManager;

import net.minecraft.client.Minecraft;

public class MacuLibClient {

	public static void init() {
		CapeManager.fetchAvailableCapes();
		PersistaClient.init();
	}
}
