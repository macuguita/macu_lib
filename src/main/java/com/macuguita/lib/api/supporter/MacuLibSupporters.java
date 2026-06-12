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
package com.macuguita.lib.api.supporter;

import java.util.UUID;

import org.jspecify.annotations.Nullable;

import com.macuguita.lib.impl.supporters.RoleChecker;

/**
 * This class provides read-only access to role information loaded from the
 * MacuLib roles system.
 *
 * <p>Role data is periodically fetched from a remote source and cached in memory.
 * <p>The period of time that it takes between fetches can be changed through {@link com.macuguita.lib.impl.MacuLibConfig }
 */
@Deprecated(forRemoval = true)
public final class MacuLibSupporters {

	private MacuLibSupporters() {}

	/**
	 * Returns the role assigned to a player, or {@code null} if the player has no role.
	 *
	 * @param playerUUID the UUID of the player
	 * @return the role name, or {@code null} if none is assigned
	 */
	public static @Nullable String getPlayerRole(UUID playerUUID) {
		return RoleChecker.getPlayerRole(playerUUID);
	}

	/**
	 * Checks whether a player has a specific role.
	 *
	 * @param playerUUID the UUID of the player
	 * @param role       the role name to check
	 * @return {@code true} if the player has the role, otherwise {@code false}
	 */
	public static boolean hasRole(UUID playerUUID, String role) {
		return RoleChecker.hasRole(playerUUID, role);
	}
}
