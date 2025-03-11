package com.macuguita.lib;

import com.macuguita.lib.platform.registry.GuitaRegistries;
import com.macuguita.lib.platform.registry.GuitaRegistry;
import com.macuguita.lib.platform.registry.GuitaRegistryEntry;
import com.macuguita.lib.supporters.RoleChecker;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MacuguitaLib {

    public static final String MOD_ID = "macu_lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GuitaRegistry<Item> ITEMS = GuitaRegistries.create(Registries.ITEM, MOD_ID);

    public static final GuitaRegistryEntry<Item> CUSTOM_ITEM = ITEMS.register("custom", () -> new Item(new Item.Settings()));

    public static void init() {
        ITEMS.init();
        RoleChecker.init();
    }

}
