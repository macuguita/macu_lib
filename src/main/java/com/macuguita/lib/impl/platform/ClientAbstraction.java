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
package com.macuguita.lib.impl.platform;

import dev.yumi.commons.event.Event;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import com.macuguita.lib.api.event.player.client.ClientPlayerJoinEvent;
import com.macuguita.lib.api.event.player.client.ClientPlayerLeaveEvent;

@ApiStatus.Internal
public interface ClientAbstraction {

	ClientAbstraction INSTANCE =
		Util.make(
			() -> {
				try {
					return (ClientAbstraction)
						Class.forName(
								"com.macuguita.lib.impl.platform."
									+ (CommonAbstraction.IS_FABRIC
									? "fabric.FabricClientAbstraction"
									: "neoforge.NeoClientAbstraction"))
							.getField("INSTANCE")
							.get(null);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			});

	static ClientAbstraction get() {
		return INSTANCE;
	}

	void registerPlayerJoinEvent(Event<Identifier, ClientPlayerJoinEvent> event);

	void registerPlayerLeaveEvent(Event<Identifier, ClientPlayerLeaveEvent> event);

	<T extends CustomPacketPayload> void registerGlobalReceiverPlay(
		CustomPacketPayload.Type<T> type, PlayPacketReceiver<T> receiver);

	interface PlayPacketReceiver<T> {
		void receive(Minecraft minecraft, LocalPlayer player, T payload);
	}
}
