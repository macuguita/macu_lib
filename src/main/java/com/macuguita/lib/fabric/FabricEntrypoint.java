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

package com.macuguita.lib.fabric;

//? fabric {

import com.macuguita.lib.MacuLib;

import com.macuguita.supporters.RoleChecker;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import org.jetbrains.annotations.ApiStatus;

import net.fabricmc.api.ModInitializer;

@ApiStatus.Internal
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
		MacuLib.init();

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> RoleChecker.shutdown()); // Sif i do my own even system I should probably change this to common
	}

}
//?}
