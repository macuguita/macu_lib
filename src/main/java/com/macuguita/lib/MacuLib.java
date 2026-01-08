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
		LOGGER.info("Initializing {} on {}", MOD_ID, Platform.INSTANCE.loader());
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
