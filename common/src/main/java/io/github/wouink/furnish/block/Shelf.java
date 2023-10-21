package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.ShelfTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
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



public class Shelf extends HorizontalDirectionalBlock implements EntityBlock {
	private static final VoxelShape[] SHELF = VoxelShapeHelper.getRotatedShapes(Block.box(0, 2, 0, 5, 4, 16));
	public Shelf(Properties p) {
		super(p.noOcclusion());
		FurnishBlocks.Shelves.add(this);
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
		return new ShelfTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		InteractionResult resultType = InteractionResult.FAIL;
		if(!world.isClientSide()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShelfTileEntity) {
				playerEntity.setItemInHand(hand, ((ShelfTileEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
				resultType = InteractionResult.SUCCESS;
			}
		}
		return resultType == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof ShelfTileEntity) {
			ItemStack stack = ((ShelfTileEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(world, pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		System.out.println("onRemove");
		if(!state.is(newState.getBlock())) {
			System.out.println("pas le meme block");
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShelfTileEntity) {
				System.out.println("shelf");
				ItemStack stack = ((ShelfTileEntity) tileEntity).getHeldItem();
				if (!stack.isEmpty()) {
					System.out.println("stack not empty");
					world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
				}
			}
			super.onRemove(state, world, pos, newState, moving);
		}
	}
}
