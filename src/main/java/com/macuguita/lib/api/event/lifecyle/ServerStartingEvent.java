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
package com.macuguita.lib.api.event.lifecyle;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;

public interface ServerStartingEvent {

	/**
	 * Called when a Minecraft server is starting.
	 *
	 * <p>This occurs before the {@link PlayerList player list} and any levels are loaded.
	 */
	Event<Identifier, ServerStartingEvent> EVENT = YumiEvents.EVENTS.create(ServerStartingEvent.class, listeners -> (server) -> {
		for (var listener : listeners) {
			listener.serverStarting(server);
		}
	});

	void serverStarting(MinecraftServer server);
}
