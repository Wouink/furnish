package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


public class Awning extends HorizontalDirectionalBlock {
	public static final VoxelShape AWNING_SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	public static final BooleanProperty LEFT = BooleanProperty.create("left");
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");

	public Awning(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LEFT, false).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, LEFT, RIGHT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Direction dir = ctx.getHorizontalDirection();
		Level world = ctx.getLevel();
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
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		if(!dir.getAxis().isVertical()) {
			BlockState scan = world.getBlockState(pos.relative(state.getValue(FACING).getClockWise()));
			state = state.setValue(LEFT, (scan.getBlock() instanceof Awning && scan.getValue(FACING) == state.getValue(FACING)));
			scan = world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise()));
			state = state.setValue(RIGHT, (scan.getBlock() instanceof Awning && scan.getValue(FACING) == state.getValue(FACING)));
		}
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return Shapes.block();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return AWNING_SHAPE;
	}
}
