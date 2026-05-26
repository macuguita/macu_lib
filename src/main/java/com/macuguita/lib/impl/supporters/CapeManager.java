/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.impl.supporters;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import com.mojang.blaze3d.platform.NativeImage;

import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
public final class CapeManager {

    private static final String CAPE_BASE_URL =
        "https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/capes/";

    private static final Map<String, Identifier> LOADED_CAPES =
        new Object2ObjectLinkedOpenHashMap<>();
    private static final Set<String> LOADING_CAPES = ConcurrentHashMap.newKeySet();
    private static final int TIMEOUT_MS = 5000;

    private CapeManager() {}

    public static @Nullable Identifier getPlayerCape(UUID playerUUID) {
        String role = RoleChecker.getPlayerRole(playerUUID);

        if (role == null) {
            return null;
        }

        return getCapeForRole(role);
    }

    public static @Nullable Identifier getCapeForRole(String role) {
        String capeUrl = CAPE_BASE_URL + role + ".png";
        return getCape(capeUrl);
    }

    public static boolean hasCape(UUID playerUUID) {
        return RoleChecker.getPlayerRole(playerUUID) != null;
    }

    private static @Nullable Identifier getCape(String urlString) {
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
        Identifier id = Identifier.fromNamespaceAndPath(MacuLib.MOD_ID, filename);

        Identifier textureLocation =
            Identifier.fromNamespaceAndPath(MacuLib.MOD_ID, "textures/" + filename + ".png");

        LOADING_CAPES.add(urlString);

        CompletableFuture.runAsync(
            () -> {
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
                        MacuLib.LOGGER.error(
                            "Failed to fetch cape from {}: HTTP {}", urlString, responseCode);
                        LOADING_CAPES.remove(urlString);
                        return;
                    }

                    try (InputStream inputStream = connection.getInputStream()) {
                        NativeImage image = NativeImage.read(inputStream);

                        if (image.getWidth() != 64 || image.getHeight() != 32) {
                            MacuLib.LOGGER.warn(
                                "Cape texture has unexpected dimensions: {}x{} (expected 64x32)",
                                image.getWidth(),
                                image.getHeight());
                        }

                        Minecraft.getInstance()
                            .execute(
                                () -> {
                                    DynamicTexture texture =
                                        new DynamicTexture(() -> "DynamicCape" + id, image);
                                    Minecraft.getInstance()
                                        .getTextureManager()
                                        .register(textureLocation, texture);
                                    LOADED_CAPES.put(urlString, id);
                                    LOADING_CAPES.remove(urlString);
                                    MacuLib.LOGGER.info(
                                        "Successfully loaded cape: {} (registered at: {})",
                                        id,
                                        textureLocation);
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
