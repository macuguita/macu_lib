/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
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
