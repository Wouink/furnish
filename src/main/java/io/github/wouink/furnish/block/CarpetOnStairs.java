package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.util.INoBlockItem;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

public class CarpetOnStairs extends HorizontalBlock implements INoBlockItem {
	public static final VoxelShape[] CARPET_SHAPE = VoxelShapeHelper.getMergedShapes(
			VoxelShapeHelper.getRotatedShapes(Block.box(8, 0, 0, 16, 1, 16)),
			VoxelShapeHelper.getRotatedShapes(Block.box(7, 1, 0, 8, -8, 16)),
			VoxelShapeHelper.getRotatedShapes(Block.box(0, -8, 0, 7, -7, 16))
	);
	private final Block clone;
	public CarpetOnStairs(Properties p, String registryName, Block _clone) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		clone = _clone;
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return CARPET_SHAPE[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState newState, IWorld world, BlockPos pos, BlockPos newPos) {
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, newState, world, pos, newPos);
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader reader, BlockPos pos) {
		return (reader.getBlockState(pos.below()).getBlock() instanceof StairsBlock);
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader reader, BlockPos pos, BlockState state) {
		return new ItemStack(clone);
	}
}
