package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IronGate extends HorizontalDirectionalBlock {
	public static final MapCodec<IronGate> CODEC = simpleCodec(IronGate::new);
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
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
			return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		Direction.Axis axis = dir.getAxis();
		if (state.getValue(FACING).getClockWise().getAxis() != axis) {
			return super.updateShape(state, dir, fromState, world, pos, fromPos);
		} else {
			return state;
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		if (state.getValue(OPEN)) {
			return Shapes.empty();
		} else {
			return state.getValue(FACING).getAxis() == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		Level level = ctx.getLevel();
		BlockPos blockpos = ctx.getClickedPos();
		boolean flag = level.hasNeighborSignal(blockpos);
		Direction direction = ctx.getHorizontalDirection();
		return this.defaultBlockState().setValue(FACING, direction).setValue(OPEN, Boolean.valueOf(flag)).setValue(POWERED, Boolean.valueOf(flag));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		blockState = blockState.cycle(OPEN);
		Direction direction = player.getDirection();
		if (blockState.getValue(FACING) == direction.getOpposite()) {
			blockState = blockState.setValue(FACING, direction);
		}
		level.setBlock(blockPos, blockState, Block.UPDATE_ALL_IMMEDIATE);

		boolean flag = blockState.getValue(OPEN);
		level.playSound(null, blockPos, flag ? FurnishRegistries.Iron_Gate_Open_Sound.get() : FurnishRegistries.Iron_Gate_Close_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
		return InteractionResult.CONSUME;	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}

	@Override
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

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN, POWERED);
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? X_SUPPORT_SHAPE : Z_SUPPORT_SHAPE;
	}
}