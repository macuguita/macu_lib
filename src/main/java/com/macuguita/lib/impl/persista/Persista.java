package com.macuguita.lib.impl.persista;

import com.macuguita.lib.MacuLib;
import com.macuguita.lib.network.NetworkManager;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApiStatus.Internal
public class Persista {

	static final Logger LOGGER = LoggerFactory.getLogger("(" + MacuLib.MOD_ID + ") persista-client");
	static final boolean HAS_INTERNET = checkInternet();
	static final Duration REQUEST_TIMEOUT = Duration.ofMillis(MacuLib.CONFIG.persista.requestTimeout);
	static @Nullable String USER_AGENT;

	public static void init() {
		USER_AGENT = MacuLib.MOD_ID + "/before-3.0.0";
		NetworkManager.registerC2S(C2SDataUpdatedPacket.TYPE, C2SDataUpdatedPacket.CODEC, (pkt, player) -> C2SDataUpdatedPacket.handle(player, pkt));
		NetworkManager.registerS2C(S2CDataUpdatedPacket.TYPE, S2CDataUpdatedPacket.CODEC);
	}

	private static boolean checkInternet() {
		try (var client = HttpClient.newHttpClient()) {
			var req = HttpRequest.newBuilder(URI.create("https://sessionserver.mojang.com")).build();
			client.send(req, HttpResponse.BodyHandlers.discarding());
			return true;
		} catch (InterruptedException | IOException e) {
			LOGGER.error("No internet access, remote fetching disabled");
			return false;
		}
	}
}
