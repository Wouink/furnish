package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Sofa extends HorizontalDirectionalBlock {

	public enum SofaType implements StringRepresentable {
		ARMCHAIR("armchair"),
		LEFT("left"),
		RIGHT("right"),
		MIDDLE("middle"),
		CORNER_LEFT("corner_left"),
		CORNER_RIGHT("corner_right");

		private final String name;

		private SofaType(String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}
	}

	private static final VoxelShape[] SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 4, 16, 16));
	private static final VoxelShape[] SITTING = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 13, 6, 16));
	private static final VoxelShape[] REST_L = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 15, 10, 3));
	private static final VoxelShape[] REST_R = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 13, 15, 10, 16));
	private static final VoxelShape[] SEAT_L = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 0, 16, 16, 4));
	private static final VoxelShape[] SEAT_R = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 12, 16, 16, 16));

	private static final VoxelShape[] MIDDLE_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING);
	private static final VoxelShape[] ARMCHAIR_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING, REST_L, REST_R);
	private static final VoxelShape[] RIGHT_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING, REST_L);
	private static final VoxelShape[] LEFT_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING, REST_R);
	private static final VoxelShape[] RIGHT_CORNER_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING, SEAT_L);
	private static final VoxelShape[] LEFT_CORNER_SHAPE = VoxelShapeHelper.getMergedShapes(SEAT, SITTING, SEAT_R);

	public static final EnumProperty<SofaType> SOFA_TYPE = EnumProperty.create("type", SofaType.class);

	public Sofa(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SOFA_TYPE, SofaType.ARMCHAIR));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SOFA_TYPE);
	}

	private BlockState setBlockState(BlockState state, Direction dir, BlockPos pos, LevelAccessor world) {
		BlockState leftState = world.getBlockState(pos.relative(dir.getCounterClockWise()));
		BlockState rightState = world.getBlockState(pos.relative(dir.getClockWise()));
		boolean left = (leftState.getBlock() instanceof Sofa) &&
				(leftState.getValue(FACING) == state.getValue(FACING) || leftState.getValue(SOFA_TYPE) == SofaType.CORNER_RIGHT);
		boolean right = (rightState.getBlock() instanceof Sofa) &&
				(rightState.getValue(FACING) == state.getValue(FACING) || rightState.getValue(SOFA_TYPE) == SofaType.CORNER_LEFT);

		if(left && right) state = state.setValue(SOFA_TYPE, SofaType.MIDDLE);
		else if(left) {
			BlockState front = world.getBlockState(pos.relative(state.getValue(FACING)));
			if((front.getBlock() instanceof Sofa) && front.getValue(FACING) == state.getValue(FACING).getClockWise()) {
				state = state.setValue(SOFA_TYPE, SofaType.CORNER_RIGHT);
			} else state = state.setValue(SOFA_TYPE, SofaType.RIGHT);
		}
		else if(right) {
			BlockState front = world.getBlockState(pos.relative(state.getValue(FACING)));
			if((front.getBlock() instanceof Sofa)  && front.getValue(FACING) == state.getValue(FACING).getCounterClockWise()) {
				state = state.setValue(SOFA_TYPE, SofaType.CORNER_LEFT);
			} else state = state.setValue(SOFA_TYPE, SofaType.LEFT);
		}
		else state = state.setValue(SOFA_TYPE, SofaType.ARMCHAIR);

		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		int index = state.getValue(FACING).ordinal() - 2;
		VoxelShape ret = ARMCHAIR_SHAPE[index];
		switch(state.getValue(SOFA_TYPE).ordinal()) {
			case 1:
				ret = LEFT_SHAPE[index];
				break;
			case 2:
				ret = RIGHT_SHAPE[index];
				break;
			case 3:
				ret = MIDDLE_SHAPE[index];
				break;
			case 4:
				ret = LEFT_CORNER_SHAPE[index];
				break;
			case 5:
				ret = RIGHT_CORNER_SHAPE[index];
				break;
			default:
				break;
		}
		return ret;
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor level, BlockPos pos, BlockPos fromPos) {
		return setBlockState(state, state.getValue(FACING).getOpposite(), pos, level);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		return setBlockState(state, ctx.getHorizontalDirection(), ctx.getClickedPos(), ctx.getLevel());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		return SeatEntity.create(world, pos, 0.2, playerEntity);
	}

	@Override
	public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float dist) {
		super.fallOn(world, state, pos, entity, dist * .5f);
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter reader, Entity entity) {
		if(entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(reader, entity);
		} else {
			bounceUp(entity);
		}
	}

	// copied from BedBlock
	private static void bounceUp(Entity entity) {
		Vec3 vector3d = entity.getDeltaMovement();
		if (vector3d.y < 0.0D) {
			double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setDeltaMovement(vector3d.x, -vector3d.y * (double) 0.66F * d0, vector3d.z);
		}
	}
}
