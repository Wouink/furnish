package io.github.wouink.furnish.item.util;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.*;

import java.util.List;

public class TooltipHelper {

	// copied from Shulker Box
	public static void appendInventoryContent(ItemStack stack, List<ITextComponent> tooltip) {
		CompoundNBT compoundnbt = stack.getTagElement("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("LootTable", 8)) {
				tooltip.add(new StringTextComponent("???????"));
			}

			if (compoundnbt.contains("Items", 9)) {
				NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(compoundnbt, inventory);
				int i = 0;
				int j = 0;

				for(ItemStack itemstack : inventory) {
					if (!itemstack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							IFormattableTextComponent iformattabletextcomponent = itemstack.getHoverName().copy();
							iformattabletextcomponent.withStyle(TextFormatting.GRAY)
									.append(" x").withStyle(TextFormatting.GRAY)
									.append(String.valueOf(itemstack.getCount())).withStyle(TextFormatting.GRAY);
							tooltip.add(iformattabletextcomponent);
						}
					}
				}

				if (j - i > 0) {
					tooltip.add((new TranslationTextComponent("container.shulkerBox.more", j - i)).withStyle(TextFormatting.ITALIC));
				}
			}
		}
	}
}
