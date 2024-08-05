package io.github.wouink.furnish.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.PlacementHelper;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class Shutter extends HorizontalDirectionalBlock {
	public static final MapCodec<Shutter> CODEC = simpleCodec(Shutter::new);
	private static final VoxelShape[] SHUTTER_CLOSED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 2, 16, 16));
	private static final VoxelShape[] SHUTTER_HALF_OPENED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 16, 16, 16));
	private static final VoxelShape[] SHUTTER_HALF_OPENED_R = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 16, 16, 2));
	private static final VoxelShape[] SHUTTER_OPENED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 2, 16, 30));
	private static final VoxelShape[] SHUTTER_OPENED_R = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, -14, 2, 16, 2));

	private static final VoxelShape[] INTERACT_HALF = VoxelShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_HALF_OPENED);
	private static final VoxelShape[] INTERACT_HALF_R = VoxelShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_HALF_OPENED_R);
	private static final VoxelShape[] INTERACT_OPEN = VoxelShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_OPENED);
	private static final VoxelShape[] INTERACT_OPEN_R = VoxelShapeHelper.getMergedShapes(SHUTTER_CLOSED, SHUTTER_OPENED_R);

	public enum State implements StringRepresentable {
		CLOSED("closed"),
		HALF_OPEN("half_open"),
		OPEN("open");

		private final String name;
		private State(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}

	public static final BooleanProperty RIGHT = FurnishBlocks.CustomProperties.RIGHT;
	public static final EnumProperty<State> STATE = EnumProperty.create("state", State.class);

	public Shutter(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STATE, State.CLOSED).setValue(RIGHT, false));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, STATE, RIGHT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(RIGHT, PlacementHelper.placeRight(ctx));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.setBlock(blockPos, blockState.cycle(STATE), Block.UPDATE_ALL)) {
			level.playSound(player, blockPos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 1.0f, 1.0f);

			// update shutters in the same column
			boolean rightProp = level.getBlockState(blockPos).getValue(RIGHT).booleanValue();
			BlockPos scan = blockPos.below();
			while(level.getBlockState(scan).getBlock() == this && level.getBlockState(scan).getValue(RIGHT) == rightProp) {
				level.setBlock(scan, level.getBlockState(blockPos), Block.UPDATE_ALL);
				scan = scan.below();
			}
			scan = blockPos.above();
			while(level.getBlockState(scan).getBlock() == this && level.getBlockState(scan).getValue(RIGHT) == rightProp) {
				level.setBlock(scan, level.getBlockState(blockPos), Block.UPDATE_ALL);
				scan = scan.above();
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected ImmutableMap<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> p_152459_) {
		return super.getShapeForEachState(p_152459_);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		int index = state.getValue(FACING).ordinal() - 2;
		if(state.getValue(STATE) == State.HALF_OPEN) {
			return state.getValue(RIGHT) ? INTERACT_HALF_R[index] : INTERACT_HALF[index];
		} else if(state.getValue(STATE) == State.OPEN) {
			return state.getValue(RIGHT) ? INTERACT_OPEN_R[index] : INTERACT_OPEN[index];
		} else {
			return SHUTTER_CLOSED[index];
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		int index = state.getValue(FACING).ordinal() - 2;
		if(state.getValue(STATE) == State.HALF_OPEN) {
			return state.getValue(RIGHT) ? SHUTTER_HALF_OPENED_R[index] : SHUTTER_HALF_OPENED[index];
		} else if(state.getValue(STATE) == State.OPEN) {
			return state.getValue(RIGHT) ? SHUTTER_OPENED_R[index] : SHUTTER_OPENED[index];
		} else {
			return SHUTTER_CLOSED[index];
		}
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
		return Shapes.empty();
	}
}
