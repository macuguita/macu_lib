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
package com.macuguita.lib.impl;

import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.YumiMods;
import dev.yumi.mc.core.api.entrypoint.ModInitializer;
import folk.sisby.kaleido.api.WrappedConfig;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStartingEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppedEvent;
import com.macuguita.lib.api.event.lifecyle.ServerStoppingEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerJoinEvent;
import com.macuguita.lib.api.event.player.server.ServerPlayerLeaveEvent;
import com.macuguita.lib.impl.platform.CommonAbstraction;
import com.macuguita.lib.impl.supporters.RoleChecker;

@ApiStatus.Internal
public class MacuLib implements ModInitializer {
	public static final String MOD_ID = "macu_lib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MacuLibConfig CONFIG = WrappedConfig.createToml(YumiMods.get().getConfigDirectory(), "", MOD_ID, MacuLibConfig.class);

	@Override
	public void onInitialize(ModContainer mod) {
		RoleChecker.init();

		registerEvents();
	}

	private void registerEvents() {
		CommonAbstraction.get().registerServerStartingEvent(ServerStartingEvent.EVENT);
		CommonAbstraction.get().registerServerStartedEvent(ServerStartedEvent.EVENT);
		CommonAbstraction.get().registerServerStoppingEvent(ServerStoppingEvent.EVENT);
		CommonAbstraction.get().registerServerStoppedEvent(ServerStoppedEvent.EVENT);

		CommonAbstraction.get().registerPlayerJoinEvent(ServerPlayerJoinEvent.EVENT);
		CommonAbstraction.get().registerPlayerLeaveEvent(ServerPlayerLeaveEvent.EVENT);

		CommonAbstraction.get().registerModifyCreativeTabOutputEvent(ModifyCreativeTabOutputEvent.EVENT);
	}
}
