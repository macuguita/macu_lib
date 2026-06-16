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
package com.macuguita.lib.impl.client;

import dev.yumi.mc.core.api.ModContainer;
import dev.yumi.mc.core.api.entrypoint.client.ClientModInitializer;
import org.jetbrains.annotations.ApiStatus;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;
import com.macuguita.lib.api.network.PacketRegistry;
import com.macuguita.lib.impl.persista.ClientboundDataUpdatedPacket;
import com.macuguita.lib.impl.platform.ClientAbstraction;
import com.macuguita.lib.impl.supporters.CapeManager;

@ApiStatus.Internal
public class MacuLibClient implements ClientModInitializer {

	@Override
	public void onInitializeClient(ModContainer mod) {
		registerEvents();

		CapeManager.fetchAvailableCapes();

		PacketRegistry.registerClientboundPacketHandler(ClientboundDataUpdatedPacket.TYPE, ClientboundDataUpdatedPacket::handle);
	}

	private void registerEvents() {
		ClientAbstraction.get().registerPlayerJoinEvent(ClientPlayerJoinEvent.EVENT);
		ClientAbstraction.get().registerPlayerLeaveEvent(ClientPlayerLeaveEvent.EVENT);
	}
}
