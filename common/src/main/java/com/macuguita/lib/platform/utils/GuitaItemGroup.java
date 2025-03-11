package com.macuguita.lib.platform.utils;

import com.macuguita.lib.platform.registry.GuitaRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class GuitaItemGroup {

    public final Identifier id;
    public Supplier<ItemStack> icon;
    public boolean hideScrollBar;
    public boolean hideTitle;

    public final List<Supplier<Stream<ItemStack>>> contents = new ArrayList<>();

    public GuitaItemGroup(Identifier id) {
        this.id = id;
    }

    public GuitaItemGroup setItemIcon(Supplier<? extends ItemConvertible> icon) {
        return setStackIcon(() -> new ItemStack(icon.get()));
    }

    public GuitaItemGroup setStackIcon(Supplier<ItemStack> icon) {
        this.icon = icon;
        return this;
    }

    public GuitaItemGroup hideTitle() {
        this.hideTitle = true;
        return this;
    }

    public GuitaItemGroup hideScrollBar() {
        this.hideScrollBar = true;
        return this;
    }

    public <I extends ItemConvertible, T extends GuitaRegistry<I>> GuitaItemGroup addRegistry(T registry) {
        return addContent(() -> registry.boundStream().map(ItemStack::new));
    }

    public GuitaItemGroup addStack(Supplier<ItemStack> stack) {
        return addContent(() -> Stream.of(stack.get()));
    }

    public GuitaItemGroup addStack(ItemStack stack) {
        return addStack(() -> stack);
    }

    public GuitaItemGroup addStack(ItemConvertible item) {
        return addStack(new ItemStack(item));
    }

    public GuitaItemGroup addContent(Supplier<Stream<ItemStack>> content) {
        this.contents.add(content);
        return this;
    }

    public ItemGroup build() {
        return create(this);
    }

    @ExpectPlatform
    private static ItemGroup create(GuitaItemGroup tab) {
        throw new AssertionError();
    }


}