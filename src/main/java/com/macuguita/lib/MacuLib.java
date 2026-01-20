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

import com.macuguita.supporters.RoleChecker;
import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApiStatus.Internal
public class MacuLib {

	public static final String MOD_ID = "macu_lib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MacuLibConfig CONFIG = WrappedConfig.createToml(Platform.INSTANCE.getConfigDir(), "", MOD_ID, MacuLibConfig.class);

	public static void init() {
		RoleChecker.init();
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
