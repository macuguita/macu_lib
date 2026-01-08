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
