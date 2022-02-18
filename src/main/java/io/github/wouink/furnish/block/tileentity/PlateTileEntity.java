package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlateTileEntity extends StackHoldingTileEntity {
	public PlateTileEntity(BlockPos pos, BlockState state) {
		super(FurnishData.TileEntities.TE_Plate.get(), pos, state);
	}
}
