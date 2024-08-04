package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.blockentity.PlateBlockEntity;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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



public class Plate extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<Plate> CODEC = simpleCodec(Plate::new);
	public static final VoxelShape PLATE_SHAPE = Block.box(1, 0, 1, 15, 1, 15);

	public Plate(Properties p) {
		super(p.noOcclusion().requiresCorrectToolForDrops());
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
		return PLATE_SHAPE;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		InteractionResult resultType = InteractionResult.FAIL;
		if(!level.isClientSide()) {
			if (itemStack.isEmpty() || itemStack.has(DataComponents.FOOD) || itemStack.is(FurnishRegistries.FOOD_TAG)) {
				BlockEntity blockEntity = level.getBlockEntity(blockPos);
				if (blockEntity instanceof PlateBlockEntity plate) {
					player.setItemInHand(interactionHand, plate.swap(itemStack));
					resultType = InteractionResult.SUCCESS;
				}
			}
		}
		return InteractionHelper.toItem(resultType == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(level.isClientSide()) : resultType);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		return InteractionResult.CONSUME;
	}

	@Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlateBlockEntity(pos, state);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		BlockEntity tileEntity = levelReader.getBlockEntity(blockPos);
		if(tileEntity instanceof PlateBlockEntity) {
			ItemStack stack = ((PlateBlockEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(levelReader, blockPos, blockState);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof PlateBlockEntity) {
				ItemStack stack = ((PlateBlockEntity) tileEntity).getHeldItem();
				if (!stack.isEmpty()) {
					world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
				}
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
