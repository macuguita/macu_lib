package com.macuguita.lib.platform.utils.forge;

import com.macuguita.lib.platform.utils.GuitaItemGroup;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.fuel.forge.FuelRegistryImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class GuitaItemGroupImpl {

    private static final Map<String, DeferredRegister<ItemGroup>> CREATIVE_TABS = new ConcurrentHashMap<>();

    public static Supplier<ItemGroup> create(GuitaItemGroup tab) {
        return Entry.of(tab);
    }

    private static RegistryObject<ItemGroup> register(Identifier id, Supplier<ItemGroup> tab) {
        var register = CREATIVE_TABS.computeIfAbsent(id.getNamespace(), namespace -> {
            var registry = DeferredRegister.create(Registries.ITEM_GROUP.getKey(), namespace);
            registry.register(FMLJavaModLoadingContext.get().getModEventBus());
            return registry;
        });
        return register.register(id.getPath(), tab);
    }

    private record Entry(RegistryObject<ItemGroup> builtTab) implements Supplier<ItemGroup> {

        public static Entry of(GuitaItemGroup tab) {
            var creativeTab = ItemGroup.builder()
                    .icon(() -> tab.icon.get())
                    .displayName(Text.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
            if (tab.hideScrollBar) creativeTab.noScrollbar();
            if (tab.hideTitle) creativeTab.noRenderedName();
            creativeTab.entries((params, output) -> {
                tab.registries.forEach(registry -> registry.boundStream().forEach(output::add));
                tab.stacks.stream().map(Supplier::get).forEach(output::add);

                tab.contents.stream().flatMap(Supplier::get).forEach(output::add);
            });
            return new Entry(register(tab.id, creativeTab::build));
        }

        @Override
        public ItemGroup get() {
            return builtTab.get();
        }
    }
}
