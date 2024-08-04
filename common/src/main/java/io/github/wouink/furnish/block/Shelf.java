package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.blockentity.ShelfBlockEntity;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;



public class Shelf extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<Shelf> CODEC = simpleCodec(Shelf::new);
	private static final VoxelShape[] SHELF = VoxelShapeHelper.getRotatedShapes(Block.box(0, 2, 0, 5, 4, 16));
	public Shelf(Properties p) {
		super(p.noOcclusion());
		FurnishBlocks.Shelves.add(this);
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHELF[state.getValue(FACING).ordinal() - 2];
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ShelfBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		return InteractionHelper.fromItem(useItemOn(ItemStack.EMPTY, blockState, level, blockPos, player, InteractionHand.MAIN_HAND, blockHitResult));
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		ItemInteractionResult result = ItemInteractionResult.CONSUME;
		if(!level.isClientSide()) {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if (blockEntity instanceof ShelfBlockEntity shelf) {
				player.setItemInHand(interactionHand, shelf.swap(itemStack));
				result = ItemInteractionResult.SUCCESS;
			}
		}
		return result;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		BlockEntity blockEntity = levelReader.getBlockEntity(blockPos);
		if(blockEntity instanceof ShelfBlockEntity shelf) {
			ItemStack stack = shelf.getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(levelReader, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(!state.is(newState.getBlock())) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShelfBlockEntity) {
				ItemStack stack = ((ShelfBlockEntity) tileEntity).getHeldItem();
				if (!stack.isEmpty()) {
					world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
				}
			}
			super.onRemove(state, world, pos, newState, moving);
		}
	}
}
