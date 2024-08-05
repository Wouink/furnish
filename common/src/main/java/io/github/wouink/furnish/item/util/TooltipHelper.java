package io.github.wouink.furnish.item.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.Iterator;
import java.util.List;

public class TooltipHelper {

	// copied from Shulker Box
	public static void appendInventoryContent(ItemStack itemStack, List<Component> list) {
		int i = 0;
		int j = 0;
		Iterator var7 = ((ItemContainerContents)itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).nonEmptyItems().iterator();

		while(var7.hasNext()) {
			ItemStack itemStack2 = (ItemStack)var7.next();
			++j;
			if (i <= 4) {
				++i;
				list.add(Component.translatable("container.shulkerBox.itemCount", new Object[]{itemStack2.getHoverName(), itemStack2.getCount()}));
			}
		}

		if (j - i > 0) {
			list.add(Component.translatable("container.shulkerBox.more", new Object[]{j - i}).withStyle(ChatFormatting.ITALIC));
		}

	}
}
