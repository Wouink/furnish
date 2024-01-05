package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlateBlockEntity extends StackHoldingBlockEntity {
	public PlateBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Plate_BlockEntity.get(), pos, state);
	}
}
