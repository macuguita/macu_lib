/*
 * Copyright (c) 2026 macuguita
 *
 * Licensed under the EUPL-1.2
 * SPDX-License-Identifier: EUPL-1.2
 */
package com.macuguita.lib.test.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.world.level.ItemLike;

@Mixin(targets = "net.minecraft.world.item.CreativeModeTab$Output")
public interface CreativeModeTabOutputAccessor {

    @Invoker("accept")
    void macu_lib_test$accept(final ItemLike item);
}
