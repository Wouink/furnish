package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShowcaseTileEntity extends StackHoldingTileEntity {
	public ShowcaseTileEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Showcase_BlockEntity.get(), pos, state);
	}
}
