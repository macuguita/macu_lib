package com.macuguita.lib.platform.registry;
import com.macuguita.lib.platform.registry.builtin.ResourcefulBlockRegistry;
import com.macuguita.lib.platform.registry.builtin.ResourcefulItemRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registry;
import org.apache.commons.lang3.NotImplementedException;

public class ResourcefulRegistries {

    public static <T> ResourcefulRegistry<T> create(ResourcefulRegistry<T> parent) {
        return new ResourcefulRegistryChild<>(parent);
    }

    public static ResourcefulItemRegistry createForItems(String id) {
        return new ResourcefulItemRegistry(id);
    }

    public static ResourcefulItemRegistry createForItems(ResourcefulRegistry<Item> parent) {
        return new ResourcefulItemRegistry(ResourcefulRegistries.create(parent));
    }

    public static ResourcefulBlockRegistry createForBlocks(String id) {
        return new ResourcefulBlockRegistry(id);
    }

    public static ResourcefulBlockRegistry createForBlocks(ResourcefulRegistry<Block> parent) {
        return new ResourcefulBlockRegistry(ResourcefulRegistries.create(parent));
    }

    @ExpectPlatform
    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static <D, T extends ResourcefulRegistry<D>> T create(ResourcefulRegistryType<D, T> type, String id) {
        throw new NotImplementedException();
    }
}
