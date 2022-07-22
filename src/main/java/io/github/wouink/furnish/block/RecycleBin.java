package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RecycleBin extends Block {
	private static final VoxelShape SHAPE = Block.box(2.5, 0, 2.5, 13.5, 16, 13.5);

	public RecycleBin(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}
}
