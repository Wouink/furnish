package io.github.wouink.furnish.item.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TooltipHelper {

	// copied from Shulker Box
	public static void appendInventoryContent(ItemStack stack, List<Component> tooltip) {
		CompoundTag compoundnbt = stack.getTagElement("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("LootTable", 8)) {
				tooltip.add(new TextComponent("???????"));
			}

			if (compoundnbt.contains("Items", 9)) {
				NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
				ContainerHelper.loadAllItems(compoundnbt, inventory);
				int i = 0;
				int j = 0;

				for(ItemStack itemstack : inventory) {
					if (!itemstack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							MutableComponent mutablecomponent = itemstack.getHoverName().copy();
							mutablecomponent.append(" x").append(String.valueOf(itemstack.getCount()));
							tooltip.add(mutablecomponent);
						}
					}
				}

				if (j - i > 0) {
					tooltip.add((new TranslatableComponent("container.shulkerBox.more", j - i)).withStyle(ChatFormatting.ITALIC));
				}
			}
		}
	}
}
