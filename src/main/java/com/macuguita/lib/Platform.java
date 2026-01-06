package com.macuguita.lib;

//? fabric {
import com.macuguita.lib.fabric.FabricPlatformImpl;
//?}
//? neoforge {
/*import com.macuguita.lib.neoforge.NeoForgePlatformImpl;
*///?}
import java.nio.file.Path;
import com.macuguita.lib.reg.GuitaRegistry;
import net.minecraft.core.Registry;

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

}
