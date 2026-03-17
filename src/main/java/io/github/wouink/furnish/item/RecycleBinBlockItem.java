package io.github.wouink.furnish.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class RecycleBinBlockItem extends BlockItem {
    public RecycleBinBlockItem(Block block) {
        super(block, new Properties().setId(...)); // TODO
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
        consumer.accept(Component.translatable("block.furnish.recycle_bin.tooltip.1").withStyle(ChatFormatting.GRAY));
        consumer.accept(Component.translatable("block.furnish.recycle_bin.tooltip.2").withStyle(ChatFormatting.GRAY));
    }
}
