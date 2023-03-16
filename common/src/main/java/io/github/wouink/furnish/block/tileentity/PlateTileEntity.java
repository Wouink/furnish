package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlateTileEntity extends StackHoldingTileEntity {
	public PlateTileEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Plate_BlockEntity.get(), pos, state);
	}
}
