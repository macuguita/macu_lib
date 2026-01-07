package com.macuguita.supporters;

import com.macuguita.lib.MacuLib;
import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class CapeUtil {

    private static final Map<String, Identifier> LOADED_CAPES = new Object2ObjectLinkedOpenHashMap<>();
    private static final Set<String> LOADING_CAPES = ConcurrentHashMap.newKeySet();
    private static final int TIMEOUT_MS = 5000;

    public static @Nullable Identifier getCape(String urlString) {
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

        // The actual texture location where it needs to be registered
        Identifier textureLocation = Identifier.fromNamespaceAndPath(MacuLib.MOD_ID, "textures/" + filename + ".png");

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
                        DynamicTexture texture = new DynamicTexture(() -> "DynamicCape" + id, image);
                        Minecraft.getInstance().getTextureManager().register(textureLocation, texture);
                        LOADED_CAPES.put(urlString, id);
                        LOADING_CAPES.remove(urlString);
                        MacuLib.LOGGER.info("Successfully loaded cape: {} (registered at: {})", id, textureLocation);
                    });
                }
            } catch (Exception e) {
                MacuLib.LOGGER.error("Failed to load cape from URL: {}", urlString, e);
                LOADING_CAPES.remove(urlString);
            }
        });

        return null; // Return null initially, will be available on next call after loading
    }
}