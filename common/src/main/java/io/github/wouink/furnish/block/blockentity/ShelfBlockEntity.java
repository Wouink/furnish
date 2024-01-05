package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShelfBlockEntity extends StackHoldingBlockEntity {
	public ShelfBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Shelf_BlockEntity.get(), pos, state);
	}
}
