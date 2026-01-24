package io.github.wouink.furnish.event;

import io.github.wouink.furnish.block.CarpetOnStairs;
import io.github.wouink.furnish.block.CarpetOnTrapdoor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.phys.BlockHitResult;

public class PlaceCarpet {

    public static InteractionResult rightClickOnStairs(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.PASS;
        ItemStack inHand = player.getItemInHand(hand);
        if(inHand.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof WoolCarpetBlock carpet) {
            BlockPos pos = blockHitResult.getBlockPos();
            if(CarpetOnStairs.attemptPlacement(level, pos, carpet) || CarpetOnTrapdoor.attemptPlacement(level, pos, carpet)) {
                if(!player.isCreative()) {
                    inHand.shrink(1);
                    player.setItemInHand(hand, inHand);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
