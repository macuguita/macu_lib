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
package com.macuguita.lib.api.event.player.server;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerLeaveEvent {

	/**
	 * An event that is called when a player has joined the game. This includes loading a singleplayer
	 * world.
	 *
	 * <p>This event is called on the server thread after the player has fully been loaded into the
	 * world.
	 */
	Event<Identifier, ServerPlayerLeaveEvent> EVENT = YumiEvents.EVENTS.create(ServerPlayerLeaveEvent.class, listeners -> (serverPlayer) -> {
		for (var listener : listeners) {
			listener.playerLeave(serverPlayer);
		}
	});

	void playerLeave(ServerPlayer serverPlayer);
}
