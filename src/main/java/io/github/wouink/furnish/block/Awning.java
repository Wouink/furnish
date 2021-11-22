package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Awning extends HorizontalBlock {
	public static final VoxelShape AWNING_SHAPE = Block.box(0, 5, 0, 16, 7, 16);
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	public Awning(Properties p, String registryName) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEFT, false).setValue(RIGHT, false));
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, LEFT, RIGHT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		Direction dir = ctx.getHorizontalDirection();
		World world = ctx.getLevel();
		BlockState state = this.defaultBlockState().setValue(FACING, dir.getOpposite());

		// right awning check
		BlockState scanState = world.getBlockState(ctx.getClickedPos().relative(dir.getClockWise()));
		if(scanState.getBlock() == this && scanState.getValue(FACING) == state.getValue(FACING)) {
			state = state.setValue(RIGHT, true);
		}

		// left awning check
		scanState = world.getBlockState(ctx.getClickedPos().relative(dir.getCounterClockWise()));
		if(scanState.getBlock() == this && scanState.getValue(FACING) == state.getValue(FACING)) {
			state = state.setValue(LEFT, true);
		}
		return state;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, IWorld world, BlockPos pos, BlockPos fromPos) {
		if(!dir.getAxis().isVertical()) {
			BlockState scan = world.getBlockState(pos.relative(state.getValue(FACING).getClockWise()));
			state = state.setValue(LEFT, (scan.getBlock() instanceof Awning && scan.getValue(FACING) == state.getValue(FACING)));
			scan = world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()));
			state = state.setValue(RIGHT, (scan.getBlock() instanceof Awning && scan.getValue(FACING) == state.getValue(FACING)));
		}
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return AWNING_SHAPE;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader reader, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public float getShadeBrightness(BlockState state, IBlockReader reader, BlockPos pos) {
		return 1.0f;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}
}
