package com.macuguita.lib;

//? fabric {
import com.macuguita.lib.fabric.FabricPlatformImpl;
//?}
//? neoforge {
/*import com.macuguita.lib.neoforge.NeoForgePlatformImpl;
*///?}
import java.nio.file.Path;

import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

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

    <T> GuitaRegistry<T> createGuitaRegistry(Registry<T> registry, String id);

    void sendToServer(CustomPacketPayload payload);
    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);
    <T extends CustomPacketPayload> void registerC2S(NetworkManager.C2SRegistration<T> reg);
    <T extends CustomPacketPayload> void registerS2C(NetworkManager.S2CRegistration<T> reg);

}
