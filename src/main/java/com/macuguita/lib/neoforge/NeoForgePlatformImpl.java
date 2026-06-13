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

package com.macuguita.lib.neoforge;

//? neoforge {

/*import java.nio.file.Path;
import java.util.function.Consumer;

import com.macuguita.lib.Platform;
import com.macuguita.lib.neoforge.network.NeoForgeNetworkBootstrap;
import com.macuguita.lib.neoforge.reg.NeoForgeGuitaRegistry;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;

import net.neoforged.api.distmarker.Dist;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
//? >= 1.21.11 {
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import com.macuguita.lib.neoforge.network.NeoForgeClientNetworkBootstrap;
//?} else {
/^import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
^///?}
import net.neoforged.neoforge.network.PacketDistributor;

@ApiStatus.Internal
public class NeoForgePlatformImpl implements Platform {

	@Override
	public boolean isModLoaded(String modid) {
		return ModList.get().isLoaded(modid);
	}

	@Override
	public String loader() {
		return "neoforge";
	}

	@Override
	public Path getConfigDir() {
		return FMLPaths.CONFIGDIR.get();
	}

	@Override
	public boolean isDevelopment() {
		//? if >= 1.21.11 {
		return !FMLEnvironment.isProduction();
		//?} else {
		/^return !FMLEnvironment.production;
		 ^///?}
	}

	@Override
	public <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id) {
		return new NeoForgeGuitaRegistry<>(registry, id);
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		//? >= 1.21.11 {
		ClientPacketDistributor.sendToServer(payload);
		//?} else {
		/^PacketDistributor.sendToServer(payload);
		 ^///?}
	}

	@Override
	public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	@Override
	public <T extends CustomPacketPayload> void registerC2S(
			NetworkManager.C2SRegistration<T> reg
	) {
		NeoForgeNetworkBootstrap.C2S.add(reg);
	}

	@Override
	public <T extends CustomPacketPayload> void registerS2C(
			NetworkManager.S2CRegistration<T> reg
	) {
		NeoForgeNetworkBootstrap.S2C.add(reg);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends CustomPacketPayload> void registerClientS2CHandler(
			CustomPacketPayload.Type<T> type,
			Consumer<T> handler
	) {
		//? >= 1.21.11 {
		NeoForgeClientNetworkBootstrap.CLIENT_HANDLERS.add(
				new NeoForgeClientNetworkBootstrap.ClientHandlerRegistration<>(type, handler)
		);
		//?} else {
		/^synchronized (NeoForgeNetworkBootstrap.S2C) {
			NetworkManager.S2CRegistration<?> reg = NeoForgeNetworkBootstrap.S2C.stream()
					.filter(r -> r.type().equals(type))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException(
							"Cannot add handler: payload type " + type + " is not registered!"
					));

			if (reg.handlerSupplier() != null) {
				throw new IllegalStateException(
						"Cannot add handler: payload type " + type + " already has a handler!"
				);
			}

			NeoForgeNetworkBootstrap.S2C.remove(reg);
			NeoForgeNetworkBootstrap.S2C.add(new NetworkManager.S2CRegistration<>(
					(CustomPacketPayload.Type<CustomPacketPayload>) reg.type(),
					(StreamCodec<RegistryFriendlyByteBuf, CustomPacketPayload>) reg.codec(),
					() -> (Consumer<CustomPacketPayload>) handler
			));
		}
		^///?}
	}

	@Override
	public boolean isClient() {
		//? >= 1.21.11 {
		return FMLEnvironment.getDist() == Dist.CLIENT;
		//?} else {
		/^return FMLEnvironment.dist == Dist.CLIENT;
		^///?}
	}
}
*///?}
