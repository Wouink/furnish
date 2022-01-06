package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.ShelfTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Shelf extends HorizontalBlock {
	private static final VoxelShape[] SHELF = VoxelShapeHelper.getRotatedShapes(Block.box(0, 2, 0, 5, 4, 16));
	public Shelf(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHELF[state.getValue(FACING).ordinal() - 2];
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ShelfTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		ActionResultType resultType = ActionResultType.FAIL;
		if(!world.isClientSide()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShelfTileEntity) {
				playerEntity.setItemInHand(hand, ((ShelfTileEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
				resultType = ActionResultType.SUCCESS;
			}
		}
		return resultType == ActionResultType.SUCCESS ? ActionResultType.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
		TileEntity tileEntity = world.getBlockEntity(pos);
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
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		super.onRemove(state, world, pos, newState, moving);
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof ShelfTileEntity) {
			ItemStack stack = ((ShelfTileEntity) tileEntity).getHeldItem();
			if(!stack.isEmpty()) {
				world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
			}
		}
	}
}
