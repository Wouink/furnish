package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetOnStairs extends HorizontalDirectionalBlock {
	public static final VoxelShape[] CARPET_SHAPE = VoxelShapeHelper.getMergedShapes(
			VoxelShapeHelper.getRotatedShapes(Block.box(8, 0, 0, 16, 1, 16)),
			VoxelShapeHelper.getRotatedShapes(Block.box(7, -8, 0, 8, 1, 16)),
			VoxelShapeHelper.getRotatedShapes(Block.box(0, -8, 0, 7, -7, 16))
	);
	private final Block clone;
	public CarpetOnStairs(Properties p, Block _clone) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
		clone = _clone;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return CARPET_SHAPE[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		return !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, dir, fromState, world, pos, fromPos);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return (reader.getBlockState(pos.below()).getBlock() instanceof StairBlock);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		return new ItemStack(clone);
	}
}
