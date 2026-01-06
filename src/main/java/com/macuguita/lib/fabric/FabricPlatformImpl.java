package com.macuguita.lib.fabric;

//? fabric {
import com.macuguita.lib.Platform;
import com.macuguita.lib.fabric.reg.FabricGuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;

import java.nio.file.Path;

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

}
//?}