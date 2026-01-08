package com.macuguita.lib.neoforge;

//? neoforge {

import com.macuguita.lib.Platform;
import com.macuguita.lib.neoforge.network.NeoForgeNetworkBootstrap;
import com.macuguita.lib.neoforge.reg.NeoForgeGuitaRegistry;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.nio.file.Path;

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
        return !FMLEnvironment.isProduction();
    }

    @Override
    public <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id) {
        return new NeoForgeGuitaRegistry<>(registry, id);
    }

    //-----------------------------//
    // Networking                  //
    //-----------------------------//
    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPacketDistributor.sendToServer(payload);
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

}
//?}