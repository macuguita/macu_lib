package com.macuguita.lib.fabric;

//? fabric {

import com.macuguita.lib.Platform;
import com.macuguita.lib.fabric.reg.FabricGuitaRegistry;
import com.macuguita.lib.network.NetworkManager;
import com.macuguita.lib.reg.GuitaRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;

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
        PayloadTypeRegistry.serverboundPlay().register(reg.type(), reg.codec());
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
        PayloadTypeRegistry.clientboundPlay().register(reg.type(), reg.codec());
        ClientPlayNetworking.registerGlobalReceiver(
                reg.type(),
                (payload, context) -> {
                    var handler = reg.handlerSupplier().get();
                    handler.accept(payload);
                }
        );
    }
}
//?}
