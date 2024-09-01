package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class KnockOnDoor {
	public static EventResult onDoorHit(Player player, InteractionHand hand, BlockPos pos, Direction face) {
		Level level = player.getCommandSenderWorld();
		if(level.isClientSide() || player.isCreative()) return EventResult.pass();
		if(player.getItemInHand(hand).isEmpty()) {
			BlockState hitBlock = level.getBlockState(pos);
			if(hitBlock.getBlock() instanceof DoorBlock && hitBlock.is(FurnishRegistries.CAN_KNOCK_ON)) {
				level.playSound(null, pos, hitBlock.is(Blocks.IRON_DOOR) ? FurnishRegistries.Iron_Door_Knock_Sound.get() : FurnishRegistries.Wooden_Door_Knock_Sound.get(), SoundSource.BLOCKS, 1.0f ,1.0f);
				return EventResult.interruptTrue();
			}
		}

		return EventResult.pass();
	}
}
