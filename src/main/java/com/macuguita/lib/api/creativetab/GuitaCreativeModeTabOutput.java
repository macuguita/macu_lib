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
package com.macuguita.lib.api.creativetab;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * This interface allows the output of {@linkplain CreativeModeTab creative mode tabs} to be
 * modified in a cross-platform manner via {@link com.macuguita.lib.api.event.creativetab.ModifyCreativeTabOutputEvent}.
 */
public interface GuitaCreativeModeTabOutput extends CreativeModeTab.Output {

	/**
	 * Adds a stack to the beginning of the creative mode tab. Duplicate stacks will be removed.
	 *
	 * @param stack      the stack to add
	 * @param visibility determines whether the stack will be shown in the tab itself, returned
	 *                   for searches, or both
	 */
	void prepend(ItemStack stack, CreativeModeTab.TabVisibility visibility);

	/**
	 * Adds stacks after an existing item in the tab, or at the end if the item isn't in the tab.
	 *
	 * @param afterLast  add {@code newStacks} after the last entry of this item in the tab
	 * @param newStacks  the stacks to add
	 * @param visibility determines whether the stacks will be shown in the tab itself, returned
	 *                   for searches, or both
	 */
	void insertAfter(ItemLike afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility);

	/**
	 * Adds stacks after an existing stack in the tab, or at the end if the stack isn't in the tab.
	 *
	 * @param afterLast  add {@code newStacks} after the last tab entry matching this stack
	 *                   (compared using {@link ItemStack#isSameItemSameComponents})
	 * @param newStacks  the stacks to add
	 * @param visibility determines whether the stacks will be shown in the tab itself, returned
	 *                   for searches, or both
	 */
	void insertAfter(ItemStack afterLast, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility);

	/**
	 * Adds stacks before an existing item in the tab, or at the end if the item isn't in the tab.
	 *
	 * @param beforeFirst add {@code newStacks} before the first entry of this item in the tab
	 * @param newStacks   the stacks to add
	 * @param visibility  determines whether the stacks will be shown in the tab itself, returned
	 *                    for searches, or both
	 */
	void insertBefore(ItemLike beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility);

	/**
	 * Adds stacks before an existing stack in the tab, or at the end if the stack isn't in the tab.
	 *
	 * @param beforeFirst add {@code newStacks} before the first tab entry matching this stack
	 *                    (compared using {@link ItemStack#isSameItemSameComponents})
	 * @param newStacks   the stacks to add
	 * @param visibility  determines whether the stacks will be shown in the tab itself, returned
	 *                    for searches, or both
	 */
	void insertBefore(ItemStack beforeFirst, Collection<ItemStack> newStacks, CreativeModeTab.TabVisibility visibility);

	/**
	 * Adds a stack to the end of the creative mode tab using
	 * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS}.
	 *
	 * @param stack the stack to add
	 * @see CreativeModeTab.Output#accept(ItemStack, CreativeModeTab.TabVisibility)
	 */
	default void accept(ItemStack stack) {
		accept(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * Adds a stack to the end of the creative mode tab using
	 * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS}.
	 * Automatically creates an {@link ItemStack} from the given item.
	 *
	 * @param item the item to add
	 * @see CreativeModeTab.Output#accept(ItemStack, CreativeModeTab.TabVisibility)
	 */
	default void accept(ItemLike item) {
		accept(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * Adds a stack to the end of the creative mode tab.
	 * Automatically creates an {@link ItemStack} from the given item.
	 *
	 * @param item       the item to add
	 * @param visibility determines whether the stack will be shown in the tab itself, returned
	 *                   for searches, or both
	 * @see CreativeModeTab.Output#accept(ItemStack, CreativeModeTab.TabVisibility)
	 */
	default void accept(ItemLike item, CreativeModeTab.TabVisibility visibility) {
		accept(new ItemStack(item), visibility);
	}

	/**
	 * See {@link #prepend(ItemStack, CreativeModeTab.TabVisibility)}. Will use
	 * {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} for visibility.
	 */
	default void prepend(ItemStack stack) {
		prepend(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #prepend(ItemStack)}. Automatically creates an {@link ItemStack} from the given item.
	 */
	default void prepend(ItemLike item) {
		prepend(new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #prepend(ItemStack, CreativeModeTab.TabVisibility)}.
	 * Automatically creates an {@link ItemStack} from the given item.
	 */
	default void prepend(ItemLike item, CreativeModeTab.TabVisibility visibility) {
		prepend(new ItemStack(item), visibility);
	}

	/**
	 * See {@link #insertAfter(ItemLike, Collection, CreativeModeTab.TabVisibility)}.
	 * Will use {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} for visibility.
	 */
	default void insertAfter(ItemLike afterLast, Collection<ItemStack> newStacks) {
		insertAfter(afterLast, newStacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertAfter(ItemStack, Collection, CreativeModeTab.TabVisibility)}.
	 * Will use {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} for visibility.
	 */
	default void insertAfter(ItemStack afterLast, Collection<ItemStack> newStacks) {
		insertAfter(afterLast, newStacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertAfter(ItemLike, Collection)}.
	 */
	default void insertAfter(ItemLike afterLast, ItemStack... newStacks) {
		insertAfter(afterLast, Arrays.asList(newStacks), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertAfter(ItemStack, Collection)}.
	 */
	default void insertAfter(ItemStack afterLast, ItemStack... newStacks) {
		insertAfter(afterLast, Arrays.asList(newStacks), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertAfter(ItemLike, Collection)}.
	 * Automatically creates {@link ItemStack}s from the given items.
	 */
	default void insertAfter(ItemLike afterLast, ItemLike... newItems) {
		insertAfter(afterLast, toStacks(newItems), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertAfter(ItemStack, Collection)}.
	 * Automatically creates {@link ItemStack}s from the given items.
	 */
	default void insertAfter(ItemStack afterLast, ItemLike... newItems) {
		insertAfter(afterLast, toStacks(newItems), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemLike, Collection, CreativeModeTab.TabVisibility)}.
	 * Will use {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} for visibility.
	 */
	default void insertBefore(ItemLike beforeFirst, Collection<ItemStack> newStacks) {
		insertBefore(beforeFirst, newStacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemStack, Collection, CreativeModeTab.TabVisibility)}.
	 * Will use {@link CreativeModeTab.TabVisibility#PARENT_AND_SEARCH_TABS} for visibility.
	 */
	default void insertBefore(ItemStack beforeFirst, Collection<ItemStack> newStacks) {
		insertBefore(beforeFirst, newStacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemLike, Collection)}.
	 */
	default void insertBefore(ItemLike beforeFirst, ItemStack... newStacks) {
		insertBefore(beforeFirst, Arrays.asList(newStacks), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemStack, Collection)}.
	 */
	default void insertBefore(ItemStack beforeFirst, ItemStack... newStacks) {
		insertBefore(beforeFirst, Arrays.asList(newStacks), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemLike, Collection)}.
	 * Automatically creates {@link ItemStack}s from the given items.
	 */
	default void insertBefore(ItemLike beforeFirst, ItemLike... newItems) {
		insertBefore(beforeFirst, toStacks(newItems), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	/**
	 * See {@link #insertBefore(ItemStack, Collection)}.
	 * Automatically creates {@link ItemStack}s from the given items.
	 */
	default void insertBefore(ItemStack beforeFirst, ItemLike... newItems) {
		insertBefore(beforeFirst, toStacks(newItems), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
	}

	private static List<ItemStack> toStacks(ItemLike[] items) {
		return Arrays.stream(items).map(ItemStack::new).toList();
	}
}
