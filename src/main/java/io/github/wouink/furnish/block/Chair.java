package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Chair extends SimpleFurniture {
	public static final VoxelShape[] BASE_SHAPES = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 3, 14, 9, 13));

	private final VoxelShape[] myShapes;
	public Chair(Properties p, String registryName, VoxelShape[] shapes) {
		super(p.noOcclusion(), registryName);
		myShapes = shapes;
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
		Direction dir = state.getValue(FACING);
		return myShapes[dir.ordinal() - 2];
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		return SeatEntity.create(world, pos, 0.3, playerEntity);
	}
}
