package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChimneyConduit extends Block {

	public static final VoxelShape CONDUIT_SHAPE = Shapes.or(
			Block.box(0, 0, 0, 2, 16, 16),
			Block.box(14, 0, 0, 16, 16, 16),
			Block.box(2, 0, 0, 14, 16, 2),
			Block.box(2, 0, 14, 14, 16, 16)
	).optimize();

	public ChimneyConduit(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return CONDUIT_SHAPE;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return Shapes.block();
	}
}
