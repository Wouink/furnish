package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class PlaceCarpet {

	public static EventResult onCarpetPlaced(Level level, BlockPos pos, BlockState state, Entity placer) {
		if(level.isClientSide()) return EventResult.pass();
		if(state.getBlock() instanceof WoolCarpetBlock carpetBlock) {
			if(Registry.BLOCK.getKey(state.getBlock()).getNamespace().equals("minecraft")) {
				String color = carpetBlock.getColor().getName();
				BlockState stateBelow = level.getBlockState(pos.below());
				if(stateBelow.getBlock() instanceof StairBlock && !placer.isShiftKeyDown()) {
					if(stateBelow.getValue(StairBlock.HALF) == Half.BOTTOM && stateBelow.getValue(StairBlock.SHAPE) == StairsShape.STRAIGHT) {
						level.setBlock(pos, FurnishBlocks.Carpets_On_Stairs.get(color).get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING)), Block.UPDATE_ALL);
						return EventResult.interruptTrue();
					}
				} else if(stateBelow.getBlock() instanceof TrapDoorBlock) {
					level.setBlock(pos, FurnishBlocks.Carpets_On_Trapdoors.get(color).get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING)).setValue(BlockStateProperties.OPEN, stateBelow.getValue(BlockStateProperties.OPEN)), Block.UPDATE_ALL);
					return EventResult.interruptTrue();
				}
			}
		}
		return EventResult.pass();
	}
}
