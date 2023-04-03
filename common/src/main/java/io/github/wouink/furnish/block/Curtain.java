package io.github.wouink.furnish.block;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Curtain extends HorizontalDirectionalBlock {
	private static final VoxelShape[] CURTAIN = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 1, 16, 16));
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty LEFT = FurnishBlocks.CustomProperties.LEFT;
	public static final BooleanProperty RIGHT = FurnishBlocks.CustomProperties.RIGHT;
	public static final BooleanProperty UP = BlockStateProperties.UP;
	public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

	public Curtain(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN, POWERED, LEFT, RIGHT, UP, DOWN);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = this.defaultBlockState();
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();

		// which direction are we facing?
		state = state.setValue(FACING, ctx.getHorizontalDirection().getOpposite());

		state = calculateState(state, level, pos);

		return state;
	}

	private static BlockState calculateState(BlockState initialState, LevelAccessor level, BlockPos pos) {
		BlockState state = initialState;
		Direction facing = initialState.getValue(FACING);

		// is there a curtain on the left?
		state = state.setValue(LEFT, level.getBlockState(pos.relative(facing.getClockWise())).getBlock() instanceof Curtain);
		// is there a curtain on the right?
		state = state.setValue(RIGHT, level.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock() instanceof Curtain);
		// is there a curtain above?
		state = state.setValue(UP, level.getBlockState(pos.above()).getBlock() instanceof Curtain);
		// is there a curtain below?
		state = state.setValue(DOWN, level.getBlockState(pos.below()).getBlock() instanceof Curtain);

		return state;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		return calculateState(state, world, pos);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return CURTAIN[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return Shapes.empty();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult result) {
		if(!world.isClientSide()) setCurtainsInLine(world, pos, !state.getValue(OPEN).booleanValue());
		world.playSound(null, pos, FurnishRegistries.Curtain_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		return InteractionResult.sidedSuccess(world.isClientSide());
	}

	private static void setCurtainsInColumn(LevelAccessor level, BlockPos pos, boolean open) {
		Furnish.debug("column " + pos);
		BlockState state;

		BlockPos scan = pos;
		// look up
		while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
			level.setBlock(scan, state.setValue(OPEN, open), Block.UPDATE_ALL);
			scan = scan.above();
		}

		// look down
		scan = pos.below();
		while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
			level.setBlock(scan, state.setValue(OPEN, open), Block.UPDATE_ALL);
			scan = scan.below();
		}
	}

	private static void setCurtainsInLine(LevelAccessor level, BlockPos pos, boolean open) {
		Furnish.debug("line " + pos + " to " + open);
		Direction facing = level.getBlockState(pos).getValue(FACING);
		BlockState state;

		BlockPos scan = pos;
		// look left
		while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
			setCurtainsInColumn(level, scan, open);
			scan = scan.relative(facing.getClockWise());
		}

		// look right
		scan = pos.relative(facing.getCounterClockWise());
		while((state = level.getBlockState(scan)).getBlock() instanceof Curtain) {
			setCurtainsInColumn(level, scan, open);
			scan = scan.relative(facing.getCounterClockWise());
		}
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
		if (!level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			if (state.getValue(POWERED) != flag) {
				level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
				setCurtainsInLine(level, pos, Boolean.valueOf(flag));
				if (state.getValue(OPEN) != flag) {
					level.playSound(null, pos, FurnishRegistries.Curtain_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				}
			}
		}
	}
}
