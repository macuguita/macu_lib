/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.persista;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.impl.MacuLib;

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
