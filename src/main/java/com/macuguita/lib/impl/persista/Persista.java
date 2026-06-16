package com.macuguita.lib.impl.persista;

import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.impl.MacuLib;
import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Persista implements ModInitializer {

	static final Logger LOGGER = LoggerFactory.getLogger("(" + MacuLib.MOD_ID + ") persista-client");
	static final boolean HAS_INTERNET = checkInternet();
	static final Duration REQUEST_TIMEOUT = Duration.ofMillis(MacuLib.CONFIG.persista.requestTimeout);
	static @Nullable String USER_AGENT;

	@Override
	public void onInitialize(ModContainer mod) {
		USER_AGENT = mod.id() + "/" + mod.getVersionString();
		PacketRegistry.registerServerboundPlayPacket(ServerboundDataUpdatedPacket.TYPE, ServerboundDataUpdatedPacket.CODEC, ServerboundDataUpdatedPacket::handle);
		PacketRegistry.registerClientboundPlayPacket(ClientboundDataUpdatedPacket.TYPE, ClientboundDataUpdatedPacket.CODEC);
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
