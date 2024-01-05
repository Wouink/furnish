package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.blockentity.PlateBlockEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
	public static final VoxelShape PLATE_SHAPE = Block.box(1, 0, 1, 15, 1, 15);

	public Plate(Properties p) {
		super(p.noOcclusion().requiresCorrectToolForDrops());
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
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		InteractionResult resultType = InteractionResult.FAIL;
		if(!world.isClientSide()) {
			if (playerEntity.getItemInHand(hand).isEmpty() || playerEntity.getItemInHand(hand).isEdible() || playerEntity.getItemInHand(hand).is(FurnishRegistries.FOOD_TAG)) {
				BlockEntity tileEntity = world.getBlockEntity(pos);
				if (tileEntity instanceof PlateBlockEntity) {
					playerEntity.setItemInHand(hand, ((PlateBlockEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
					resultType = InteractionResult.SUCCESS;
				}
			}
		}
		return resultType == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(world.isClientSide()) : resultType;
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PlateBlockEntity(pos, state);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof PlateBlockEntity) {
			ItemStack stack = ((PlateBlockEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(world, pos, state);
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
