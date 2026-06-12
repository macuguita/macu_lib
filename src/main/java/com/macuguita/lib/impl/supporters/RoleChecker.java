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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.yumi.mc.core.api.ModContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

import com.macuguita.lib.impl.MacuLib;

@Deprecated(forRemoval = true)
@ApiStatus.Internal
public final class RoleChecker {

	private static final String ROLES_URL =
		"https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/supporters.json";
	private static final int CACHE_REFRESH_INTERVAL_MINUTES =
		MacuLib.CONFIG.supporters.roleCheckerMinutesInterval;

	private static volatile Map<String, Set<UUID>> cachedRoles = new HashMap<>();
	private static @Nullable String userAgent;

	private RoleChecker() {}

	public static void init(ModContainer mod) {
		userAgent = mod.id() + "/" + mod.getVersionString();
		if (CACHE_REFRESH_INTERVAL_MINUTES > 0) {
			fetchRoles().thenRunAsync(
				RoleChecker::fetchAndReschedule,
				CompletableFuture.delayedExecutor(CACHE_REFRESH_INTERVAL_MINUTES, TimeUnit.MINUTES)
			);
		} else {
			fetchRoles();
		}
	}

	private static void fetchAndReschedule() {
		fetchRoles().thenRunAsync(
			RoleChecker::fetchAndReschedule,
			CompletableFuture.delayedExecutor(CACHE_REFRESH_INTERVAL_MINUTES, TimeUnit.MINUTES)
		);
	}

	public static @Nullable String getPlayerRole(UUID playerUUID) {
		for (Map.Entry<String, Set<UUID>> entry : cachedRoles.entrySet()) {
			if (entry.getValue().contains(playerUUID)) return entry.getKey();
		}
		return null;
	}

	public static boolean hasRole(UUID playerUUID, String role) {
		return cachedRoles.containsKey(role) && cachedRoles.get(role).contains(playerUUID);
	}

	private static CompletableFuture<Void> fetchRoles() {
		try (var client = HttpClient.newBuilder()
			.followRedirects(HttpClient.Redirect.ALWAYS)
			.build()) {

			var request = HttpRequest.newBuilder(URI.create(ROLES_URL))
				.setHeader("User-Agent", userAgent)
				.GET()
				.build();

			return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
				.thenAccept(response -> {
					if (response.statusCode() != 200) {
						MacuLib.LOGGER.error("Failed to fetch roles, status: {}", response.statusCode());
						return;
					}
					try {
						JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
						JsonObject rolesObject = json.getAsJsonObject("roles");
						Map<String, Set<UUID>> newRoles = new HashMap<>();
						for (String role : rolesObject.keySet()) {
							Set<UUID> uuids = new HashSet<>();
							rolesObject.getAsJsonArray(role).forEach(element -> {
								try {
									uuids.add(UUID.fromString(element.getAsString()));
								} catch (IllegalArgumentException e) {
									MacuLib.LOGGER.error("Invalid UUID in roles JSON: {}", element.getAsString());
								}
							});
							newRoles.put(role, uuids);
						}
						cachedRoles = newRoles;
						MacuLib.LOGGER.info("Roles cache updated at {}", new Date(System.currentTimeMillis()));
					} catch (Exception e) {
						MacuLib.LOGGER.error("Failed to parse roles JSON", e);
					}
				});
		} catch (Exception e) {
			MacuLib.LOGGER.error("Exception while fetching roles", e);
			return CompletableFuture.completedFuture(null);
		}
	}
}
