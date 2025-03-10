package com.macuguita.lib.platform.registry;
import com.macuguita.lib.platform.registry.builtin.GuitaBlockRegistry;
import com.macuguita.lib.platform.registry.builtin.GuitaItemRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import org.apache.commons.lang3.NotImplementedException;

public class GuitaRegistries {

    public static <T> GuitaRegistry<T> create(GuitaRegistry<T> parent) {
        return new GuitaRegistryChild<>(parent);
    }

    public static GuitaItemRegistry createForItems(String id) {
        return new GuitaItemRegistry(id);
    }

    public static GuitaItemRegistry createForItems(GuitaRegistry<Item> parent) {
        return new GuitaItemRegistry(GuitaRegistries.create(parent));
    }

    public static GuitaBlockRegistry createForBlocks(String id) {
        return new GuitaBlockRegistry(id);
    }

    public static GuitaBlockRegistry createForBlocks(GuitaRegistry<Block> parent) {
        return new GuitaBlockRegistry(GuitaRegistries.create(parent));
    }

    @ExpectPlatform
    public static <T> GuitaRegistry<T> create(Registry<T> registry, String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <D, T extends GuitaRegistry<D>> T create(GuitaRegistryType<D, T> type, String id) {
        throw new NotImplementedException();
    }
}
