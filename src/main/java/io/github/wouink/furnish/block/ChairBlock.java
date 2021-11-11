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
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ChairBlock extends SimpleFurniture {
	public static final VoxelShape CHAIR_SHAPE = Block.box(3.0d, 0.0d, 3.0d, 13.0d, 9.0d, 13.0d);
	private static final VoxelShape SEAT_SHAPE = Block.box(3.0d, 10.0d, 10.0d, 13.0d, 16.0d, 13.0d);

	private final VoxelShape myShape;
	public ChairBlock(Properties p, String registryName, VoxelShape shape) {
		super(p.noOcclusion(), registryName);
		myShape = shape;
	}

	@Override
	public void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
		return myShape;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		return SeatEntity.create(world, pos, 0.3, playerEntity);
	}
}
