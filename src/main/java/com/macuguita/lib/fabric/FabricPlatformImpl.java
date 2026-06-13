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

package com.macuguita.lib.fabric;

//? fabric {

import java.nio.file.Path;
import java.util.function.Consumer;

import com.macuguita.lib.Platform;
import com.macuguita.lib.fabric.network.FabricClientNetworkBootstrap;
import com.macuguita.lib.fabric.reg.FabricGuitaRegistry;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;

@ApiStatus.Internal
public class FabricPlatformImpl implements Platform {

	@Override
	public boolean isModLoaded(String modid) {
		return FabricLoader.getInstance().isModLoaded(modid);
	}

	@Override
	public String loader() {
		return "fabric";
	}

	@Override
	public Path getConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	@Override
	public boolean isDevelopment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id) {
		return new FabricGuitaRegistry<>(registry, id);
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		ClientPlayNetworking.send(payload);
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	@Override
	public <T extends CustomPacketPayload> void registerC2S(
			NetworkManager.C2SRegistration<T> reg
	) {
		PayloadTypeRegistry./*? >= 26.1 {*/serverboundPlay/*?} else {*//*playC2S*//*?}*/().register(reg.type(), reg.codec());
		ServerPlayNetworking.registerGlobalReceiver(
				reg.type(),
				(payload, context) -> {
					var handler = reg.handlerSupplier().get();
					handler.accept(payload, context.player());
				}
		);
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2C(
			NetworkManager.S2CRegistration<T> reg
	) {
		PayloadTypeRegistry./*? >= 26.1 {*/clientboundPlay/*?} else {*//*playS2C*//*?}*/().register(reg.type(), reg.codec());
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT // This should be safe because it only returns true when it is on the client jar
				&& reg.handlerSupplier() != null) {
			FabricClientNetworkBootstrap.registerS2CHandler(
					reg.type(),
					(payload, context) -> {
						var handler = reg.handlerSupplier().get();
						handler.accept(payload);
					}
			);
		}
	}

	@Override
	public <T extends CustomPacketPayload> void registerClientS2CHandler(
			CustomPacketPayload.Type<T> type,
			Consumer<T> handler
	) {
		ClientPlayNetworking.registerGlobalReceiver(
				type,
				(payload, context) -> handler.accept(payload)
		);
	}
}
//?}
