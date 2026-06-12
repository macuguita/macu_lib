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
package com.macuguita.lib.impl.supporters;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.serialization.Codec;

import com.macuguita.lib.api.persista.DataToken;
import com.macuguita.lib.api.persista.PersistaAPI;
import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
public final class CapeManager {

	private static final String CAPE_BASE_URL =
		"https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/capes/";
	private static final String CAPE_LIST_URL =
		"https://api.github.com/repos/macuguita/macuguita-website/contents/capes";
	private static final int TIMEOUT_MS = 5000;

	public static final DataToken<List<String>> ENTITLEMENTS = PersistaAPI.register(
		Identifier.fromNamespaceAndPath("persista", "entitlements"),
		Codec.STRING.listOf().fieldOf("values").codec()
	);

	public static final DataToken<SupporterData> SUPPORTER_DATA = PersistaAPI.register(
		Identifier.fromNamespaceAndPath("macu_lib", "supporter"),
		SupporterData.CODEC
	);

	private static final Map<String, Identifier> LOADED_CAPES = new Object2ObjectLinkedOpenHashMap<>();
	private static final Set<String> LOADING_CAPES = ConcurrentHashMap.newKeySet();

	private static volatile List<String> availableCapes = List.of();

	private CapeManager() {}

	public static boolean isSupporter(UUID playerId) {
		return ENTITLEMENTS.getOrDefault(playerId, List.of())
			.contains("macu_lib:supporter");
	}

	public static List<String> getAvailableCapes() {
		return availableCapes;
	}

	public static void fetchAvailableCapes() {
		CompletableFuture.runAsync(() -> {
			try {
				var uri = new URI(CAPE_LIST_URL);
				var connection = (HttpURLConnection) uri.toURL().openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/vnd.github+json");
				connection.setConnectTimeout(TIMEOUT_MS);
				connection.setReadTimeout(TIMEOUT_MS);
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					MacuLib.LOGGER.error("Failed to fetch cape list: HTTP {}", connection.getResponseCode());
					return;
				}

				var json = new String(connection.getInputStream().readAllBytes());
				// parse the array of file objects, extract names ending in .png
				var names = new ArrayList<String>();
				var arr = JsonParser.parseString(json).getAsJsonArray();
				for (var el : arr) {
					var name = el.getAsJsonObject().get("name").getAsString();
					if (name.endsWith(".png")) {
						names.add(name.replace(".png", ""));
					}
				}
				availableCapes = List.copyOf(names);
				MacuLib.LOGGER.info("Loaded {} available capes: {}", availableCapes.size(), availableCapes);
			} catch (Exception e) {
				MacuLib.LOGGER.error("Failed to fetch available capes", e);
			}
		});
	}

	public static @Nullable Identifier getPlayerCape(UUID playerId) {
		if (!isSupporter(playerId)) return null;

		var supporterData = SUPPORTER_DATA.getOrDefault(playerId, SupporterData.EMPTY);
		if (supporterData.selectedCape() == null) return null;

		return loadCapeTexture(supporterData.selectedCape());
	}

	public static boolean hasCape(UUID playerId) {
		return getPlayerCape(playerId) != null;
	}

	public static void setSelectedCape(String capeName) {
		SUPPORTER_DATA.setData(new SupporterData(capeName));
	}

	@Nullable
	private static Identifier loadCapeTexture(String capeName) {
		var url = CAPE_BASE_URL + capeName + ".png";

		if (LOADED_CAPES.containsKey(url)) return LOADED_CAPES.get(url);
		if (LOADING_CAPES.contains(url)) return null;

		var hash = Integer.toHexString(url.hashCode());
		var id = MacuLib.id("capes/" + hash);
		var textureLocation = MacuLib.id("textures/capes/" + hash + ".png");

		LOADING_CAPES.add(url);

		CompletableFuture.runAsync(() -> {
			try {
				var uri = new URI(url);
				var connection = (HttpURLConnection) uri.toURL().openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(TIMEOUT_MS);
				connection.setReadTimeout(TIMEOUT_MS);
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					MacuLib.LOGGER.error("Failed to fetch cape {}: HTTP {}", capeName, connection.getResponseCode());
					LOADING_CAPES.remove(url);
					return;
				}

				var image = NativeImage.read(connection.getInputStream());
				Minecraft.getInstance().execute(() -> {
					var texture = new DynamicTexture(() -> "DynamicCape" + id, image);
					Minecraft.getInstance().getTextureManager().register(textureLocation, texture);
					LOADED_CAPES.put(url, id);
					LOADING_CAPES.remove(url);
					MacuLib.LOGGER.info("Loaded cape: {}", capeName);
				});
			} catch (Exception e) {
				MacuLib.LOGGER.error("Failed to load cape: {}", capeName, e);
				LOADING_CAPES.remove(url);
			}
		});

		return null;
	}
}
