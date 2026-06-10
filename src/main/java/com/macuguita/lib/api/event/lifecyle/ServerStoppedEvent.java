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

public interface ServerStoppedEvent {

	/**
	 * Called when a Minecraft server has stopped. All levels have been closed and all (block)entities
	 * and players have been unloaded.
	 *
	 * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin
	 * stopping, but its client may continue to run. Meanwhile, for a {@link
	 * net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
	 */
	Event<Identifier, ServerStoppedEvent> EVENT = YumiEvents.EVENTS.create(ServerStoppedEvent.class, listeners -> (server) -> {
		for (var listener : listeners) {
			listener.serverStopped(server);
		}
	});

	void serverStopped(MinecraftServer server);
}
