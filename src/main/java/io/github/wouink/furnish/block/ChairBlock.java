package io.github.wouink.furnish.block;

import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ChairBlock extends SimpleFurniture {
	private static final VoxelShape CHAIR_SHAPE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 8.0d, 16.0d);
	public ChairBlock(Properties p, String registryName) {
		super(p.noOcclusion(), registryName);
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return CHAIR_SHAPE;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		return SeatEntity.create(world, pos, 0.3, playerEntity);
	}
}
