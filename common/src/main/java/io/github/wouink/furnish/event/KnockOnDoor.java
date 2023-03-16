package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class KnockOnDoor {

	// todo is sound still played twice? (with forge events, the event is also fired when releasing mouse button)
	public static EventResult onDoorHit(Player player, InteractionHand hand, BlockPos pos, Direction face) {
		Level level = player.getLevel();
		if(level.isClientSide() || player.isCreative()) return EventResult.pass();
		if(player.getItemInHand(hand).isEmpty()) {
			BlockState hitBlock = level.getBlockState(pos);
			if(hitBlock.getBlock() instanceof DoorBlock) {
				level.playSound(null, pos, hitBlock.getMaterial() == Material.METAL ? FurnishData.Sounds.Iron_Door_Knock.get() : FurnishData.Sounds.Wooden_Door_Knock.get(), SoundSource.BLOCKS, 1.0f ,1.0f);
				return EventResult.interruptTrue();
			}
		}

		return EventResult.interruptFalse();
	}
}
