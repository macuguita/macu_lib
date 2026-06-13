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


import java.nio.file.Path;
import java.util.function.Consumer;

//? fabric {
import com.macuguita.lib.fabric.FabricPlatformImpl;
//?}
//? neoforge {
/*import com.macuguita.lib.neoforge.NeoForgePlatformImpl;
*///?}
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface Platform {

	//? fabric {
	Platform INSTANCE = new FabricPlatformImpl();
	 //?}
	//? neoforge {
	/*Platform INSTANCE = new NeoForgePlatformImpl();
	*///?}


	boolean isModLoaded(String modid);

	String loader();

	Path getConfigDir();

	boolean isDevelopment();

	boolean isClient();

	<T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id);

	void sendToServer(CustomPacketPayload payload);

	void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);

	<T extends CustomPacketPayload> void registerC2S(NetworkManager.C2SRegistration<T> reg);

	<T extends CustomPacketPayload> void registerS2C(NetworkManager.S2CRegistration<T> reg);

	<T extends CustomPacketPayload> void registerClientS2CHandler(CustomPacketPayload.Type<T> type, Consumer<T> handler);

}
