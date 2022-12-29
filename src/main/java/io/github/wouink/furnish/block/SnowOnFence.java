package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.INoBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SnowOnFence extends Block implements INoBlockItem {
	public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	public static final BooleanProperty EAST = BlockStateProperties.EAST;
	public static final BooleanProperty WEST = BlockStateProperties.WEST;
	public static final BooleanProperty GROUND = BooleanProperty.create("ground");

	public static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 2, 10);
	public static final VoxelShape SHAPE_GROUND = Shapes.or(SHAPE, Block.box(0.0D, -16D, 0.0D, 16.0D, -14D, 16.0D));

	public SnowOnFence(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(NORTH, SOUTH, EAST, WEST, GROUND);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return Shapes.empty();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
		if(below.getBlock() instanceof FenceBlock) {
			return getConnections(super.getStateForPlacement(ctx), ctx.getLevel(), ctx.getClickedPos())
					.setValue(GROUND, onGround(ctx.getLevel(), ctx.getClickedPos()));
		}
		return null;
	}

	private boolean onGround(LevelAccessor world, BlockPos pos) {
		System.out.println("onGround called");
		return world.getBlockState(pos.below().below()).isFaceSturdy(world, pos.below().below(), Direction.UP);
	}

	private BlockState getConnections(BlockState state, LevelAccessor world, BlockPos pos) {
		BlockState below = world.getBlockState(pos.below());
		if(!(below.getBlock() instanceof FenceBlock)) return state;
		return state.setValue(NORTH, below.getValue(FenceBlock.NORTH))
				.setValue(SOUTH, below.getValue(FenceBlock.SOUTH))
				.setValue(EAST, below.getValue(FenceBlock.EAST))
				.setValue(WEST, below.getValue(FenceBlock.WEST));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return state.getValue(GROUND).booleanValue() ? SHAPE_GROUND : SHAPE;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return (world.getBlockState(pos.below()).getBlock() instanceof FenceBlock);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		System.out.println("updateShape called for pos " + pos);
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : getConnections(super.updateShape(state, dir, newState, world, pos, fromPos), world, pos).setValue(GROUND, onGround(world, pos));
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return new ItemStack(Blocks.SNOW);
	}
}
