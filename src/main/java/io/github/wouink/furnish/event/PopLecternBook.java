package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;

public class PopLecternBook {

    public static InteractionResult onLecternAttacked(Player player, Level level, InteractionHand hand, BlockPos blockPos, Direction direction) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(player.isCreative()) return InteractionResult.PASS; // destroys the block instantly
        if(player.isSpectator()) return InteractionResult.PASS;
        if(!level.getBlockState(blockPos).is(Blocks.LECTERN)) return InteractionResult.PASS;
        if(!level.getBlockState(blockPos).is(FurnishContents.CAN_POP_BOOK)) return InteractionResult.PASS;
        if(!(level.getBlockEntity(blockPos) instanceof LecternBlockEntity lectern)) return InteractionResult.PASS;
        if(!lectern.hasBook()) return InteractionResult.PASS;

        ItemStack book = lectern.getBook().copy();
        lectern.clearContent(); // sets changed
        LecternBlock.resetBookState(player, level, blockPos, lectern.getBlockState(), false);
        // Containers.dropContents(level, blockPos, NonNullList.of(book)); doesn't work for some reason
        level.addFreshEntity(new ItemEntity(level, blockPos.getX() + .5, blockPos.getY() + 1, blockPos.getZ() + .5, book));
        level.playSound(null, blockPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.PLAYERS);
        return InteractionResult.PASS;
    }
}
