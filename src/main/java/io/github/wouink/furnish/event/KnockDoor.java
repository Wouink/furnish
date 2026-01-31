package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;

public class KnockDoor {

    public static InteractionResult onDoorAttacked(Player player, Level level, InteractionHand hand, BlockPos blockPos, Direction direction) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(player.isSpectator()) return InteractionResult.PASS;
        if(player.isCreative()) return InteractionResult.PASS; // destroys the block immediately
        if(!player.getItemInHand(hand).isEmpty()) return InteractionResult.PASS;
        if(!(level.getBlockState(blockPos).getBlock() instanceof DoorBlock doorBlock)) return InteractionResult.PASS;
        if(!level.getBlockState(blockPos).is(FurnishContents.CAN_KNOCK_ON)) return InteractionResult.PASS;

        SoundEvent sound = (doorBlock == Blocks.IRON_DOOR) ? FurnishContents.KNOCK_IRON_DOOR : FurnishContents.KNOCK_WOODEN_DOOR;
        level.playSound(null, blockPos, sound, SoundSource.PLAYERS);
        return InteractionResult.PASS;
    }
}
