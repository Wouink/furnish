package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.Half;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class CarpetOnTrapdoor extends HorizontalBlock {
	private static final VoxelShape CLOSED_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
	public static final BooleanProperty OPEN = BooleanProperty.create("open");
	private final Block clone;
	public CarpetOnTrapdoor(Properties p, String registryName, Block _clone) {
		super(p.noOcclusion());
		clone = _clone;
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, IWorld world, BlockPos pos, BlockPos fromPos) {
		BlockState below = world.getBlockState(pos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			if(below.getValue(TrapDoorBlock.HALF) == Half.TOP) return state.setValue(OPEN, below.getValue(TrapDoorBlock.OPEN));
		} else if(world.isEmptyBlock(pos.below())) return Blocks.AIR.defaultBlockState();
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(OPEN).booleanValue() ? VoxelShapes.empty() : CLOSED_SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader reader, BlockPos pos, BlockState state) {
		return new ItemStack(clone);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		BlockState below = world.getBlockState(pos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			return below.use(world, playerEntity, hand, blockRayTraceResult.withPosition(pos.below()));
		}
		return ActionResultType.FAIL;
	}
}
