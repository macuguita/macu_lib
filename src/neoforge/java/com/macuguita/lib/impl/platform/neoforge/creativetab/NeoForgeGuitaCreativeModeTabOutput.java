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
package com.macuguita.lib.impl.platform.neoforge.creativetab;

import java.util.Collection;
import java.util.List;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import com.macuguita.lib.api.creativetab.GuitaCreativeModeTabOutput;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class NeoForgeGuitaCreativeModeTabOutput implements GuitaCreativeModeTabOutput {
	private final BuildCreativeModeTabContentsEvent delegate;

	public NeoForgeGuitaCreativeModeTabOutput(BuildCreativeModeTabContentsEvent delegate) {
		this.delegate = delegate;
	}

	@Override
	public void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		delegate.accept(stack, visibility);
	}

	@Override
	public void prepend(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
		delegate.insertFirst(stack, visibility);
	}

	@Override
	public void insertAfter(ItemStack afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		if (newStacks.isEmpty()) return;

		ItemStack current = afterLast;
		for (ItemStack stack : newStacks) {
			if (!delegate.getParentEntries().contains(stack) && !delegate.getSearchEntries().contains(stack)) {
				delegate.insertAfter(current, stack, visibility);
			}
			current = stack;
		}
	}

	@Override
	public void insertAfter(ItemLike afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		if (newStacks.isEmpty()) return;

		// Find the last matching stack to use as anchor
		ItemStack anchor = delegate.getParentEntries().stream()
			.filter(s -> s.is(afterLast.asItem()))
			.reduce((first, second) -> second)
			.orElse(null);

		if (anchor == null) {
			newStacks.stream()
				.filter(s -> !delegate.getParentEntries().contains(s))
				.forEach(s -> delegate.accept(s, visibility));
			return;
		}

		insertAfter(anchor, newStacks, visibility);
	}

	@Override
	public void insertBefore(ItemStack beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		if (newStacks.isEmpty()) return;

		List<ItemStack> list = List.copyOf(newStacks);
		ItemStack current = beforeFirst;
		for (int i = list.size() - 1; i >= 0; i--) {
			ItemStack stack = list.get(i);
			if (!delegate.getParentEntries().contains(stack) && !delegate.getSearchEntries().contains(stack)) {
				delegate.insertBefore(current, stack, visibility);
			}
			current = stack;
		}
	}

	@Override
	public void insertBefore(ItemLike beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility) {
		if (newStacks.isEmpty()) return;

		ItemStack anchor = delegate.getParentEntries().stream()
			.filter(s -> s.is(beforeFirst.asItem()))
			.findFirst()
			.orElse(null);

		if (anchor == null) {
			newStacks.stream()
				.filter(s -> !delegate.getParentEntries().contains(s))
				.forEach(s -> delegate.accept(s, visibility));
			return;
		}

		insertBefore(anchor, newStacks, visibility);
	}
}
