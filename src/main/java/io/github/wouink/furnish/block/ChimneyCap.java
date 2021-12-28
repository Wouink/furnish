package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ChimneyCap extends Block {

	public static final VoxelShape CAP_SHAPE = Block.box(1, 6, 1, 15, 8, 15);

	public ChimneyCap(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return CAP_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if(CampfireBlock.isSmokeyPos(world, pos)) {
			CampfireBlock.makeParticles(world, pos.relative(Direction.Plane.HORIZONTAL.getRandomDirection(random)), false, false);
		}
	}
}
