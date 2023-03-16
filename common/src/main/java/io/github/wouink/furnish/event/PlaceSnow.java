package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.block.SnowOnFence;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class PlaceSnow {

	public static EventResult onSnowLayerUsedOnBlock(Player player, InteractionHand hand, BlockPos pos, Direction face) {
		Level level = player.getLevel();
		if(level.isClientSide() || player.isShiftKeyDown()) return EventResult.pass();
		if(!level.isEmptyBlock(pos.above())) return EventResult.pass();

		ItemStack heldItem = player.getItemInHand(hand);
		if(heldItem.getItem() != Items.SNOW) return EventResult.pass();

		BlockState state = level.getBlockState(pos);
		boolean placed = true;

		if(state.getBlock() instanceof FenceBlock) {
			level.setBlock(pos.above(), copyFenceProperties(state).setValue(SnowOnFence.GROUND, level.getBlockState(pos.below().below()).isFaceSturdy(level, pos.below().below(), Direction.UP)), Block.UPDATE_ALL);
		} else if(state.getBlock() instanceof StairBlock) {
			if(state.getValue(StairBlock.HALF) == Half.BOTTOM && state.getValue(StairBlock.SHAPE) == StairsShape.STRAIGHT) {
				level.setBlock(pos.above(), FurnishBlocks.Snow_On_Stairs.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)), Block.UPDATE_ALL);
			}
		} else placed = false;

		if(placed) {
			player.swing(hand);
			if(!player.isCreative()) {
				heldItem.shrink(1);
				player.setItemInHand(hand, heldItem);
			}
			level.playSound(null, pos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		}

		return EventResult.interruptTrue();
	}

	private static BlockState copyFenceProperties(BlockState fenceBelow) {
		BlockState snowOnFence = FurnishBlocks.Snow_On_Fence.get().defaultBlockState();
		snowOnFence.setValue(BlockStateProperties.EAST, fenceBelow.getValue(FenceBlock.EAST));
		snowOnFence.setValue(BlockStateProperties.WEST, fenceBelow.getValue(FenceBlock.WEST));
		snowOnFence.setValue(BlockStateProperties.NORTH, fenceBelow.getValue(FenceBlock.NORTH));
		snowOnFence.setValue(BlockStateProperties.SOUTH, fenceBelow.getValue(FenceBlock.SOUTH));
		return snowOnFence;
	}
}
