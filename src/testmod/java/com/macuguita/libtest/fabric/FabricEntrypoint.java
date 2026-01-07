package com.macuguita.libtest.fabric;

//? fabric {
import com.macuguita.libtest.TestMod;
import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {
    @Override
    public void onInitialize() {
        TestMod.init();
    }
}
//?}