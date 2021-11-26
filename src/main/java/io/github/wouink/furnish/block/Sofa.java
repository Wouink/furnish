package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Sofa extends HorizontalBlock {

	public enum SofaType implements IStringSerializable {
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

	public Sofa(Properties p, String registryName) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(SOFA_TYPE, SofaType.ARMCHAIR));
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, SOFA_TYPE);
	}

	private BlockState setBlockState(BlockState state, Direction dir, BlockPos pos, IWorld world) {
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
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
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
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, IWorld world, BlockPos pos, BlockPos formPos) {
		// add some particle effect to see which blocks update
		// world.addParticle(ParticleTypes.HEART, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0, 0);
		return setBlockState(state, state.getValue(FACING).getOpposite(), pos, world);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState state = defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		return setBlockState(state, ctx.getHorizontalDirection(), ctx.getClickedPos(), ctx.getLevel());
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		return SeatEntity.create(world, pos, 0.25, playerEntity);
	}
}
