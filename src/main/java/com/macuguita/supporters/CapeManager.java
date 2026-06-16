package com.macuguita.supporters;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.macuguita.lib.MacuLib;
import com.macuguita.lib.api.persista.DataToken;
import com.macuguita.lib.api.persista.PersistaAPI;
import com.mojang.serialization.Codec;
import org.jspecify.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

public class CapeManager {

	private static final String CAPE_BASE_URL =
			"https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/capes/";
	private static final String CAPE_LIST_URL =
			"https://api.github.com/repos/macuguita/macuguita-website/contents/capes";
	private static final int TIMEOUT_MS = 5000;

	public static final DataToken<List<String>> ENTITLEMENTS = PersistaAPI.register(
			ResourceLocation.fromNamespaceAndPath("persista", "entitlements"),
			Codec.STRING.listOf().fieldOf("values").codec()
	);

	public static final DataToken<SupporterData> SUPPORTER_DATA = PersistaAPI.register(
			ResourceLocation.fromNamespaceAndPath("macu_lib", "supporter"),
			SupporterData.CODEC
	);

	private static volatile List<String> availableCapes = List.of();

	private CapeManager() {}

	public static boolean isSupporter(UUID playerId) {
		return ENTITLEMENTS.getOrDefault(playerId, List.of()).contains("macu_lib:supporter");
	}

	public static @Nullable ResourceLocation getPlayerCape(UUID playerId) {
		if (!isSupporter(playerId)) return null;

		SupporterData data = SUPPORTER_DATA.getOrDefault(playerId, SupporterData.EMPTY);
		if (data.selectedCape() == null) return null;

		return CapeUtil.getCape(CAPE_BASE_URL + data.selectedCape() + ".png");
	}

	public static boolean hasCape(UUID playerId) {
		return getPlayerCape(playerId) != null;
	}

	public static void setSelectedCape(@Nullable String capeName) {
		SUPPORTER_DATA.setData(new SupporterData(capeName));
	}

	public static List<String> getAvailableCapes() {
		return availableCapes;
	}

	public static void fetchAvailableCapes() {
		CompletableFuture.runAsync(() -> {
			try {
				HttpURLConnection connection = (HttpURLConnection) new URI(CAPE_LIST_URL).toURL().openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/vnd.github+json");
				connection.setConnectTimeout(TIMEOUT_MS);
				connection.setReadTimeout(TIMEOUT_MS);
				connection.connect();

				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					MacuLib.LOGGER.error("Failed to fetch cape list: HTTP {}", connection.getResponseCode());
					return;
				}

				String json = new String(connection.getInputStream().readAllBytes());
				List<String> names = new ArrayList<>();
				com.google.gson.JsonParser.parseString(json).getAsJsonArray().forEach(el -> {
					String name = el.getAsJsonObject().get("name").getAsString();
					if (name.endsWith(".png")) names.add(name.replace(".png", ""));
				});
				availableCapes = List.copyOf(names);
				MacuLib.LOGGER.info("Loaded {} capes: {}", availableCapes.size(), availableCapes);
			} catch (Exception e) {
				MacuLib.LOGGER.error("Failed to fetch available capes", e);
			}
		});
	}
}
