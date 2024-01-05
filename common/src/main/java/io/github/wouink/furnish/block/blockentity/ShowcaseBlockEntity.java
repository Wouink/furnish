package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ShowcaseBlockEntity extends StackHoldingBlockEntity {
	public ShowcaseBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Showcase_BlockEntity.get(), pos, state);
	}
}
