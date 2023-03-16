package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShelfTileEntity extends StackHoldingTileEntity {
	public ShelfTileEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Shelf_BlockEntity.get(), pos, state);
	}
}
