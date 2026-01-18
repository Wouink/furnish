package io.github.wouink.furnish.block.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public class TooltipHelper {

    // copied from shulker box block
    private static final Component UNKNOWN_CONTENTS = Component.translatable("container.shulkerBox.unknownContents");
    public static void appendInventoryContents(ItemStack itemStack, List<Component> tooltip) {
        if (itemStack.has(DataComponents.CONTAINER_LOOT)) {
            tooltip.add(UNKNOWN_CONTENTS);
        }

        int i = 0;
        int j = 0;

        for(ItemStack itemStack2 : ((ItemContainerContents)itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)).nonEmptyItems()) {
            ++j;
            if (i <= 4) {
                ++i;
                tooltip.add(Component.translatable("container.shulkerBox.itemCount", new Object[]{itemStack2.getHoverName(), itemStack2.getCount()}));
            }
        }

        if (j - i > 0) {
            tooltip.add(Component.translatable("container.shulkerBox.more", new Object[]{j - i}).withStyle(ChatFormatting.ITALIC));
        }
    }
}
