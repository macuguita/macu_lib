package com.macuguita.supporters;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.macuguita.lib.MacuLib;
import org.jspecify.annotations.Nullable;

public class RoleChecker {

	// URL to the JSON file containing roles and UUIDs
	private static final String ROLES_URL = "https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/supporters.json";

	// Cache for roles and UUIDs
	private static Map<String, Set<UUID>> cachedRoles = new HashMap<>();

	private static final int CACHE_REFRESH_INTERVAL_MINUTES = MacuLib.CONFIG.supporters.roleCheckerMinutesInterval;

	/**
	 * Initializes the RoleChecker and starts the periodic cache refresh task.
	 */
	public static void init() {
		// Fetch roles immediately on initialization
		fetchRoles();

		// Only schedule periodic refresh if interval is positive
		if (CACHE_REFRESH_INTERVAL_MINUTES > 0) {
			// Scheduled executor service for periodic cache refresh
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
					RoleChecker::fetchRoles, // Task to run
					CACHE_REFRESH_INTERVAL_MINUTES, // Initial delay
					CACHE_REFRESH_INTERVAL_MINUTES, // Periodic delay
					TimeUnit.MINUTES // Time unit
			);
			MacuLib.LOGGER.info("RoleChecker initialized with cache refresh every " + CACHE_REFRESH_INTERVAL_MINUTES + " minutes.");
		} else {
			MacuLib.LOGGER.info("RoleChecker initialized with single fetch (no periodic refresh).");
		}
	}

	/**
	 * Fetches the roles from the JSON file and updates the cache.
	 */
	private static void fetchRoles() {
		try {
			URI uri = new URI(ROLES_URL);
			URL url = uri.toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			if (!url.getProtocol().equals("https")) {
				MacuLib.LOGGER.error("Roles URL must use HTTPS for security.");
				throw new RuntimeException("Roles URL must use HTTPS for security.");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
			reader.close();

			// Parse the "roles" object
			JsonObject rolesObject = json.getAsJsonObject("roles");
			Map<String, Set<UUID>> newRoles = new HashMap<>();
			for (String role : rolesObject.keySet()) {
				Set<UUID> uuids = new HashSet<>();
				rolesObject.getAsJsonArray(role).forEach(element -> {
					try {
						uuids.add(UUID.fromString(element.getAsString()));
					} catch (IllegalArgumentException e) {
						MacuLib.LOGGER.error("Invalid UUID in roles JSON: " + element.getAsString());
					}
				});
				newRoles.put(role, uuids);
			}

			cachedRoles = newRoles;
			MacuLib.LOGGER.info("Roles cache updated at " + new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			MacuLib.LOGGER.error("Failed to fetch roles from URL: " + ROLES_URL, e);
		}
	}

	/**
	 * Gets the role of a specific player UUID.
	 *
	 * @param playerUUID The UUID of the player to check.
	 * @return The role of the player (e.g., "developer", "admin", "supporter"), or null if the player has no role.
	 */
	public static @Nullable String getPlayerRole(UUID playerUUID) {
		for (Map.Entry<String, Set<UUID>> entry : cachedRoles.entrySet()) {
			if (entry.getValue().contains(playerUUID)) {
				return entry.getKey();
			}
		}
		return null; // Player has no role
	}

	/**
	 * Checks if a player has a specific role.
	 *
	 * @param playerUUID The UUID of the player to check.
	 * @param role       The role to check for (e.g., "developer", "admin", "supporter").
	 * @return True if the player has the role, false otherwise.
	 */
	public static boolean hasRole(UUID playerUUID, String role) {
		return cachedRoles.containsKey(role) && cachedRoles.get(role).contains(playerUUID);
	}
}
