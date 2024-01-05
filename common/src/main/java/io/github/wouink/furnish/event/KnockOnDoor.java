package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
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

			// to determine if the click is pressed or released
			// the progress of destroying the block could be helpful.
			// when == 0 it would be the click pressed, otherwise the released
			// but Minecraft does not store a destroy progress
			// instead, a packet is sent at the beginning of the destroy process and
			// the block is broken if not packet has been sent to cancel the process

			// hitBlock.getDestroyProgress = how many % of block is destroyed every tick
			Furnish.debug("Destroy progress = " + hitBlock.getDestroyProgress(player, level, pos) + ", clientSide = " + level.isClientSide());
			Furnish.debug("Level time = " + level.getGameTime());
			if(hitBlock.getBlock() instanceof DoorBlock && hitBlock.is(FurnishRegistries.CAN_KNOCK_ON)) {
				level.playSound(null, pos, hitBlock.is(Blocks.IRON_DOOR) ? FurnishRegistries.Iron_Door_Knock_Sound.get() : FurnishRegistries.Wooden_Door_Knock_Sound.get(), SoundSource.BLOCKS, 1.0f ,1.0f);
				return EventResult.interruptTrue();
			}
		}

		return EventResult.pass();
	}
}
