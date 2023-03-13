package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChimneyCap extends Block {

	public static final VoxelShape CAP_SHAPE = Block.box(1, 6, 1, 15, 8, 15);

	public ChimneyCap(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return CAP_SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if(CampfireBlock.isSmokeyPos(level, pos)) {
			CampfireBlock.makeParticles(level, pos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random)), false, false);
		}
	}
}
