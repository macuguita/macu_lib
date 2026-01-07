package com.macuguita.lib.neoforge;

//? neoforge {
/*import com.macuguita.lib.Platform;
import com.macuguita.lib.neoforge.reg.NeoForgeGuitaRegistry;
import com.macuguita.lib.reg.GuitaRegistry;
import net.minecraft.core.Registry;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;

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

}
*///?}