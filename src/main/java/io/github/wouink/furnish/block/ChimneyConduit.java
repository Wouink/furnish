package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

public class ChimneyConduit extends Block {

	public static final VoxelShape CONDUIT_SHAPE = VoxelShapes.or(
			Block.box(0, 0, 0, 2, 16, 16),
			Block.box(14, 0, 0, 16, 16, 16),
			Block.box(2, 0, 0, 14, 16, 2),
			Block.box(2, 0, 14, 14, 16, 16)
	).optimize();

	public ChimneyConduit(Properties p, String registryName) {
		super(p.noOcclusion());
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return CONDUIT_SHAPE;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return VoxelShapes.block();
	}
}
