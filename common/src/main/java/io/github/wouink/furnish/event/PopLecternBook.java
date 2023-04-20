package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class PopLecternBook {

    public static EventResult onLecternLeftClick(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.getLevel();
        if(!level.isClientSide()) {
            if(level.getBlockState(pos).is(Blocks.LECTERN) && level.getBlockState(pos).is(FurnishRegistries.CAN_POP_BOOK)) {
                if(level.getBlockEntity(pos) != null && (level.getBlockEntity(pos) instanceof LecternBlockEntity lectern)) {
                    if(lectern.hasBook()) {
                        BlockState oldBlockState = lectern.getBlockState();
                        ItemStack book = lectern.getBook();
                        lectern.clearContent();
                        lectern.setChanged();
                        LecternBlock.resetBookState(level, pos, oldBlockState, false);
                        level.addFreshEntity(new ItemEntity(level, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, book));
                        level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
                        return EventResult.interruptTrue();
                    }
                }
            }
        }
        return EventResult.pass();
    }
}
