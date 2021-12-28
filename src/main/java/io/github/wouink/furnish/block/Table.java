package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class Table extends Block {
	public static final BooleanProperty NW = BooleanProperty.create("nw");
	public static final BooleanProperty NE = BooleanProperty.create("ne");
	public static final BooleanProperty SW = BooleanProperty.create("sw");
	public static final BooleanProperty SE = BooleanProperty.create("se");

	protected static final VoxelShape BASE_SHAPE = Block.box(0d, 14d, 0d, 16d, 16d, 16d);
	protected static final VoxelShape NW_SHAPE = Block.box(1d, 0d, 1d, 4d, 14d, 4d);
	protected static final VoxelShape NE_SHAPE = Block.box(12d, 0d, 1d, 15d, 14d, 4d);
	protected static final VoxelShape SW_SHAPE = Block.box(1d, 0d, 12d, 4d, 14d, 15d);
	protected static final VoxelShape SE_SHAPE = Block.box(12d, 0d, 12d, 15d, 14d, 15d);

	public Table(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(NW, NE, SW, SE);
	}

	public BlockState getState(BlockState state, IWorld world, BlockPos pos) {
		BlockState N = world.getBlockState(pos.north());
		BlockState W = world.getBlockState(pos.west());
		BlockState S = world.getBlockState(pos.south());
		BlockState E = world.getBlockState(pos.east());
		boolean n = N.getBlock() instanceof Table;
		boolean w = W.getBlock() instanceof Table;
		boolean s = S.getBlock() instanceof Table;
		boolean e = E.getBlock() instanceof Table;

		return getStateDefinition().any()
				.setValue(NE, !n && !e)
				.setValue(NW, !n && !w)
				.setValue(SE, !s && !e)
				.setValue(SW, !s && !w);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return getState(getStateDefinition().any(), ctx.getLevel(), ctx.getClickedPos());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
        return VoxelShapes.or(BASE_SHAPE,
				state.getValue(NE).booleanValue() ? NE_SHAPE : VoxelShapes.empty(),
				state.getValue(NW).booleanValue() ? NW_SHAPE : VoxelShapes.empty(),
				state.getValue(SE).booleanValue() ? SE_SHAPE : VoxelShapes.empty(),
				state.getValue(SW).booleanValue() ? SW_SHAPE : VoxelShapes.empty()
        );
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
		return getState(state, world, pos);
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
