package com.macuguita.lib.platform.utils.fabric;

import com.macuguita.lib.platform.utils.GuitaItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class GuitaItemGroupImpl {

    public static Supplier<ItemGroup> create(GuitaItemGroup tab) {
        var group = FabricItemGroup.builder()
                .icon(() -> tab.icon.get())
                .displayName(Text.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) group.noScrollbar();
        if (tab.hideTitle) group.noRenderedName();
        group.entries((params, output) -> {
            tab.registries.forEach(registry -> registry.boundStream().forEach(output::add));
            tab.stacks.stream().map(Supplier::get).forEach(output::add);

            tab.contents.stream().flatMap(Supplier::get).forEach(output::add);
        });
        ItemGroup tab1 = group.build();
        Registry.register(Registries.ITEM_GROUP, tab.id, tab1);
        return () -> tab1;
    }
}
