package com.macuguita.lib.platform.utils.neoforge;

import com.macuguita.lib.platform.utils.GuitaItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class GuitaItemGroupImpl {
    public static ItemGroup create(GuitaItemGroup tab) {
        var creativeTab = ItemGroup.builder()
                .icon(() -> tab.icon.get())
                .displayName(Text.translatable("itemGroup." + tab.id.getNamespace() + "." + tab.id.getPath()));
        if (tab.hideScrollBar) creativeTab.noScrollbar();
        if (tab.hideTitle) creativeTab.noRenderedName();
        creativeTab.entries((params, output) -> tab.contents.stream().flatMap(Supplier::get).forEach(output::add));
        return creativeTab.build();
    }
}
