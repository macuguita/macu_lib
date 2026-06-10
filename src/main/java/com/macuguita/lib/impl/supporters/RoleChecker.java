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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import com.macuguita.lib.impl.MacuLib;

@ApiStatus.Internal
public final class RoleChecker {

	private static final String ROLES_URL =
		"https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/supporters.json";
	private static final int CACHE_REFRESH_INTERVAL_MINUTES = MacuLib.CONFIG.supporters.roleCheckerMinutesInterval;

	private static Map<String, Set<UUID>> cachedRoles = new HashMap<>();
	private static @Nullable ScheduledExecutorService executorService;

	private RoleChecker() {}

	public static void init() {
		fetchRoles();

		if (CACHE_REFRESH_INTERVAL_MINUTES > 0) {
			executorService =
				Executors.newScheduledThreadPool(
					1,
					r -> {
						Thread thread = new Thread(r);
						thread.setDaemon(true);
						return thread;
					});

			executorService.scheduleAtFixedRate(
				RoleChecker::fetchRoles,
				CACHE_REFRESH_INTERVAL_MINUTES,
				CACHE_REFRESH_INTERVAL_MINUTES,
				TimeUnit.MINUTES);
			MacuLib.LOGGER.info(
				"RoleChecker initialized with cache refresh every "
					+ CACHE_REFRESH_INTERVAL_MINUTES
					+ " minutes.");
		} else {
			MacuLib.LOGGER.info("RoleChecker initialized with single fetch (no periodic refresh).");
		}
	}

	public static void shutdown() {
		if (executorService != null && !executorService.isShutdown()) {
			executorService.shutdown();
			try {
				if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
				}
				MacuLib.LOGGER.info("RoleChecker shut down successfully.");
			} catch (InterruptedException e) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

	// Package-private — only MacuLibSupporters (via same package trick) or impl classes call these
	public static @Nullable String getPlayerRole(UUID playerUUID) {
		for (Map.Entry<String, Set<UUID>> entry : cachedRoles.entrySet()) {
			if (entry.getValue().contains(playerUUID)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static boolean hasRole(UUID playerUUID, String role) {
		return cachedRoles.containsKey(role) && cachedRoles.get(role).contains(playerUUID);
	}

	private static void fetchRoles() {
		try {
			URI uri = new URI(ROLES_URL);
			URL url = uri.toURL();

			if (!url.getProtocol().equals("https")) {
				MacuLib.LOGGER.error("Roles URL must use HTTPS for security.");
				throw new RuntimeException("Roles URL must use HTTPS for security.");
			}

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			BufferedReader reader =
				new BufferedReader(new InputStreamReader(connection.getInputStream()));
			JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
			reader.close();

			JsonObject rolesObject = json.getAsJsonObject("roles");
			Map<String, Set<UUID>> newRoles = new HashMap<>();
			for (String role : rolesObject.keySet()) {
				Set<UUID> uuids = new HashSet<>();
				rolesObject
					.getAsJsonArray(role)
					.forEach(
						element -> {
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
}
