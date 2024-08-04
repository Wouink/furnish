package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.InteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetOnTrapdoor extends HorizontalDirectionalBlock {
	private static final VoxelShape CLOSED_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	private final Block clone;
	public CarpetOnTrapdoor(Properties p, Block _clone) {
		super(p.noOcclusion());
		clone = _clone;
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		BlockState below = world.getBlockState(pos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			if(below.getValue(TrapDoorBlock.HALF) == Half.TOP) return state.setValue(OPEN, below.getValue(TrapDoorBlock.OPEN));
		} else if(world.isEmptyBlock(pos.below())) return Blocks.AIR.defaultBlockState();
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return state.getValue(OPEN).booleanValue() ? Shapes.empty() : CLOSED_SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(clone);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		BlockState below = level.getBlockState(blockPos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			return below.useWithoutItem(level, player, blockHitResult);
		}
		return InteractionResult.FAIL;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		// todo this can stay null for now
		return null;
	}
}
