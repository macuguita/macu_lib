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

package com.macuguita.lib;

import com.macuguita.lib.impl.persista.C2SDataUpdatedPacket;
import com.macuguita.lib.impl.persista.S2CDataUpdatedPacket;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.supporters.RoleChecker;
import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;

import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class MacuLib {

	public static final String MOD_ID = "macu_lib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MacuLibConfig CONFIG = WrappedConfig.createToml(Platform.INSTANCE.getConfigDir(), "", MOD_ID, MacuLibConfig.class);

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void init() {
		RoleChecker.init();

		NetworkManager.registerC2S(C2SDataUpdatedPacket.TYPE, C2SDataUpdatedPacket.CODEC, (pkt, player) -> C2SDataUpdatedPacket.handle(player, pkt));
		NetworkManager.registerS2C(S2CDataUpdatedPacket.TYPE, S2CDataUpdatedPacket.CODEC);
	}

	public static class MacuLibConfig extends WrappedConfig {

		@Comment("Config for the supporter perks")
		public Supporters supporters = new Supporters();

		public static class Supporters implements Section {
			@Comment("The amount of minutes to fetch changes in the role versions")
			@Comment("if the value is negative or 0 it only checks at startup")
			public int roleCheckerMinutesInterval = 60 * 12;
		}
	}
}
