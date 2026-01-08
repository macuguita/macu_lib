package com.macuguita.supporters;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import net.minecraft.resources.Identifier;

public class CapeManager {

	private static final String CAPE_BASE_URL = "https://raw.githubusercontent.com/macuguita/macuguita-website/refs/heads/main/capes/";

	/**
	 * Gets the cape identifier for a player based on their role.
	 *
	 * @param playerUUID The UUID of the player.
	 * @return The cape Identifier, or null if the player has no role/cape.
	 */
	public static @Nullable Identifier getPlayerCape(UUID playerUUID) {
		String role = RoleChecker.getPlayerRole(playerUUID);

		if (role == null) {
			return null; // Player has no role
		}

		return getCapeForRole(role);
	}

	/**
	 * Gets the cape identifier for a specific role.
	 *
	 * @param role The role name (e.g., "developer", "supporter").
	 * @return The cape Identifier for that role.
	 */
	public static @Nullable Identifier getCapeForRole(String role) {
		String capeUrl = CAPE_BASE_URL + role + ".png";
		return CapeUtil.getCape(capeUrl);
	}

	/**
	 * Checks if a player has a cape (i.e., has any role).
	 *
	 * @param playerUUID The UUID of the player.
	 * @return True if the player has a cape, false otherwise.
	 */
	public static boolean hasCape(UUID playerUUID) {
		return RoleChecker.getPlayerRole(playerUUID) != null;
	}
}
