package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class Shutter extends HorizontalBlock {
	private static final VoxelShape[] SHUTTER_CLOSED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 2, 16, 16));
	private static final VoxelShape[] SHUTTER_HALF_OPENED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 16, 16, 16));
	private static final VoxelShape[] SHUTTER_HALF_OPENED_R = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 16, 16, 2));
	private static final VoxelShape[] SHUTTER_OPENED = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 14, 2, 16, 30));
	private static final VoxelShape[] SHUTTER_OPENED_R = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, -14, 2, 16, 2));

	public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 2);
	public Shutter(Properties p, String registryName) {
		super(p.noOcclusion());
		setRegistryName(registryName);
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STATE, 0).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, STATE, RIGHT);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(RIGHT, ctx.getPlayer().isCrouching());
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		if(world.setBlock(pos, state.cycle(STATE), 3)) {
			world.playSound(playerEntity, pos, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 1.0f, 1.0f);
			// update shutters in the same column
			int stateProp = world.getBlockState(pos).getValue(STATE).intValue();
			boolean rightProp = world.getBlockState(pos).getValue(RIGHT).booleanValue();
			BlockPos scan = pos.below();
			while(world.getBlockState(scan).getBlock() == this && world.getBlockState(scan).getValue(RIGHT) == rightProp) {
				world.setBlock(scan, world.getBlockState(pos), 3);
				scan = scan.below();
			}
			scan = pos.above();
			while(world.getBlockState(scan).getBlock() == this && world.getBlockState(scan).getValue(RIGHT) == rightProp) {
				world.setBlock(scan, world.getBlockState(pos), 3);
				scan = scan.above();
			}
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		VoxelShape ret = SHUTTER_CLOSED[state.getValue(FACING).ordinal() - 2];
		if(state.getValue(RIGHT).booleanValue()) {
			switch (state.getValue(STATE).intValue()) {
				case 1:
					ret = SHUTTER_HALF_OPENED_R[state.getValue(FACING).ordinal() - 2];
					break;
				case 2:
					ret = SHUTTER_OPENED_R[state.getValue(FACING).ordinal() - 2];
					break;
				default:
					break;
			}
		} else {
			switch (state.getValue(STATE).intValue()) {
				case 1:
					ret = SHUTTER_HALF_OPENED[state.getValue(FACING).ordinal() - 2];
					break;
				case 2:
					ret = SHUTTER_OPENED[state.getValue(FACING).ordinal() - 2];
					break;
				default:
					break;
			}
		}
		return ret;
	}
}
