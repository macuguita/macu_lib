/*
 * macu_lib
 * Copyright (C) 2026 macuguita
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 *
 */

package com.macuguita.supporters;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import com.macuguita.lib.MacuLib;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public class CapeUtil {

	private static final Map<String, ResourceLocation> LOADED_CAPES = new Object2ObjectLinkedOpenHashMap<>();
	private static final Set<String> LOADING_CAPES = ConcurrentHashMap.newKeySet();
	private static final int TIMEOUT_MS = 5000;

	public static @Nullable ResourceLocation getCape(String urlString) {
		if (LOADED_CAPES.containsKey(urlString)) {
			return LOADED_CAPES.get(urlString);
		}

		if (LOADING_CAPES.contains(urlString)) {
			return null;
		}

		// Create identifier that matches what ResourceTexture expects
		// ResourceTexture will look for: namespace:textures/<path>.png
		// So we register as: namespace:textures/capes/<hash>.png
		String hash = Integer.toHexString(urlString.hashCode());
		String filename = "capes/" + hash;
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MacuLib.MOD_ID, filename);

		//? >= 1.21.11 {
		/*// The actual texture location where it needs to be registered
		ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(MacuLib.MOD_ID, "textures/" + filename + ".png");
		*///?}

		LOADING_CAPES.add(urlString);

		CompletableFuture.runAsync(() -> {
			try {
				URI uri = new URI(urlString);
				if (!uri.getScheme().equals("https")) {
					MacuLib.LOGGER.error("Cape URL must use HTTPS: {}", urlString);
					LOADING_CAPES.remove(urlString);
					return;
				}

				URL url = uri.toURL();
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(TIMEOUT_MS);
				connection.setReadTimeout(TIMEOUT_MS);
				connection.setDoInput(true);
				connection.connect();

				int responseCode = connection.getResponseCode();
				if (responseCode != HttpURLConnection.HTTP_OK) {
					MacuLib.LOGGER.error("Failed to fetch cape from {}: HTTP {}", urlString, responseCode);
					LOADING_CAPES.remove(urlString);
					return;
				}

				try (InputStream inputStream = connection.getInputStream()) {
					NativeImage image = NativeImage.read(inputStream);

					if (image.getWidth() != 64 || image.getHeight() != 32) {
						MacuLib.LOGGER.warn("Cape texture has unexpected dimensions: {}x{} (expected 64x32)",
								image.getWidth(), image.getHeight());
					}

					Minecraft.getInstance().execute(() -> {
						//? >= 1.21.11 {
						/*DynamicTexture texture = new DynamicTexture(() -> "DynamicCape" + id, image);
						Minecraft.getInstance().getTextureManager().register(textureLocation, texture);
						LOADED_CAPES.put(urlString, id);
						LOADING_CAPES.remove(urlString);
						MacuLib.LOGGER.info("Successfully loaded cape: {} (registered at: {})", id, textureLocation);
						*///?} else {
						DynamicTexture texture = new DynamicTexture(image);
						Minecraft.getInstance().getTextureManager().register(id, texture);
						LOADED_CAPES.put(urlString, id);
						LOADING_CAPES.remove(urlString);
						MacuLib.LOGGER.info("Successfully loaded cape: {}", id);
						//?}
					});
				}
			} catch (Exception e) {
				MacuLib.LOGGER.error("Failed to load cape from URL: {}", urlString, e);
				LOADING_CAPES.remove(urlString);
			}
		});

		return null;
	}
}
