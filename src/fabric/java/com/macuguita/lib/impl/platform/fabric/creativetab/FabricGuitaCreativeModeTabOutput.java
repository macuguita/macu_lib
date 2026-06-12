/*
 * Copyright 2026 macuguita
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package com.macuguita.lib.impl.platform.fabric.creativetab;

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTabOutput;

import com.macuguita.lib.api.creativetab.GuitaCreativeModeTabOutput;

@ApiStatus.Internal
public class FabricGuitaCreativeModeTabOutput implements GuitaCreativeModeTabOutput {
	private final FabricCreativeModeTabOutput delegate;

	public FabricGuitaCreativeModeTabOutput(FabricCreativeModeTabOutput delegate) {
		this.delegate = delegate;
	}

	@Override
	public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		delegate.accept(stack, visibility);
	}

	@Override
	public void prepend(ItemStack stack, CreativeModeTab.TabVisibility visibility) { delegate.prepend(stack, visibility); }

	@Override
	public void insertAfter(ItemLike afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		delegate.insertAfter(afterLast, newStacks, visibility);
	}

	@Override
	public void insertAfter(ItemStack afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		delegate.insertAfter(afterLast, newStacks, visibility);
	}

	@Override
	public void insertBefore(ItemLike beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		delegate.insertBefore(beforeFirst, newStacks, visibility);
	}

	@Override
	public void insertBefore(ItemStack beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		delegate.insertBefore(beforeFirst, newStacks, visibility);
	}
}
