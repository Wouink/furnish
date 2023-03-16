package io.github.wouink.furnish.block;

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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IronGate extends HorizontalDirectionalBlock {
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	private static final VoxelShape Z_SHAPE = Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
	private static final VoxelShape X_SHAPE = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D);
	private static final VoxelShape X_SUPPORT_SHAPE = Shapes.or(
			Block.box(0, 0, 0, 16, 16, 1),
			Block.box(0, 0, 15, 16, 16, 16)
	);
	private static final VoxelShape Z_SUPPORT_SHAPE = Shapes.or(
			Block.box(0, 0, 0, 1, 16, 16),
			Block.box(15, 0, 0, 16, 16, 16)
	);

	public IronGate(BlockBehaviour.Properties p) {
		super(p.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.valueOf(false)).setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
			return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
	}

	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		Direction.Axis axis = dir.getAxis();
		if (state.getValue(FACING).getClockWise().getAxis() != axis) {
			return super.updateShape(state, dir, fromState, world, pos, fromPos);
		} else {
			return state;
		}
	}

	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		if (state.getValue(OPEN)) {
			return Shapes.empty();
		} else {
			return state.getValue(FACING).getAxis() == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
		}
	}

	public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
		switch (type) {
			case LAND, AIR:
				return state.getValue(OPEN);
			default:
				return false;
		}
	}

	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Level level = ctx.getLevel();
		BlockPos blockpos = ctx.getClickedPos();
		boolean flag = level.hasNeighborSignal(blockpos);
		Direction direction = ctx.getHorizontalDirection();
		return this.defaultBlockState().setValue(FACING, direction).setValue(OPEN, Boolean.valueOf(flag)).setValue(POWERED, Boolean.valueOf(flag));
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		state = state.cycle(OPEN);
		Direction direction = player.getDirection();
		if (state.getValue(FACING) == direction.getOpposite()) {
			state = state.setValue(FACING, direction);
		}
		level.setBlock(pos, state, 10);

		boolean flag = state.getValue(OPEN);
		level.playSound(null, pos, flag ? FurnishRegistries.Iron_Gate_Open_Sound.get() : FurnishRegistries.Iron_Gate_Close_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		return InteractionResult.CONSUME;
	}

	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_53375_, BlockPos p_53376_, boolean p_53377_) {
		if (!level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			if (state.getValue(POWERED) != flag) {
				level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
				if (state.getValue(OPEN) != flag) {
					level.playSound(null, pos, flag ? FurnishRegistries.Iron_Gate_Open_Sound.get() : FurnishRegistries.Iron_Gate_Close_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				}
			}
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, POWERED);
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SUPPORT_SHAPE : Z_SUPPORT_SHAPE;
	}
}