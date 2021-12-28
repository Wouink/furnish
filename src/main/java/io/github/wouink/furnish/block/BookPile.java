package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BookPile extends HorizontalBlock {
	private static final IntegerProperty PLACEMENT = IntegerProperty.create("placement", 0, 2);
	private static final VoxelShape PILE_SHAPE = Block.box(4, 0, 4, 12, 8, 12);
	private static final VoxelShape LAYING_SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	public BookPile(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PLACEMENT, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(PLACEMENT);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(PLACEMENT).intValue() == 2 ? LAYING_SHAPE : PILE_SHAPE;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		world.setBlockAndUpdate(pos, state.cycle(PLACEMENT));
		world.playSound(playerEntity, pos, SoundEvents.BOOK_PAGE_TURN, SoundCategory.BLOCKS, 1.0f, 1.0f);
		return ActionResultType.SUCCESS;
	}

	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity) {
		return SoundType.WOOL;
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
		return 0.5f;
	}
}
