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
package com.macuguita.lib.api.event.player.client;

import dev.yumi.commons.event.Event;
import dev.yumi.mc.core.api.YumiEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.Identifier;

public interface ClientPlayerJoinEvent {

	/**
	 * An event for notification when the client play packet listener is ready to send packets to the
	 * server.
	 *
	 * <p>At this stage, the packet listener is ready to send packets to the server. Since the
	 * client's local state has been set up.
	 */
	Event<Identifier, ClientPlayerJoinEvent> EVENT = YumiEvents.EVENTS.create(ClientPlayerJoinEvent.class, listeners -> (packetListener, client) -> {
		for (var listener : listeners) {
			listener.playerJoin(packetListener, client);
		}
	});

	void playerJoin(ClientPacketListener listener, Minecraft client);
}
