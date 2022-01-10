package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.ShowcaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Showcase extends HorizontalBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public Showcase(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, POWERED);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ShowcaseTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		ActionResultType resultType = ActionResultType.FAIL;
		if(!world.isClientSide()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShowcaseTileEntity) {
				playerEntity.setItemInHand(hand, ((ShowcaseTileEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
				resultType = ActionResultType.SUCCESS;
			}
		}
		return resultType == ActionResultType.SUCCESS ? ActionResultType.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Override
	public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof ShowcaseTileEntity) {
			ItemStack stack = ((ShowcaseTileEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(world, pos, state);
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		if(!state.is(newState.getBlock())) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof ShowcaseTileEntity) {
				ItemStack stack = ((ShowcaseTileEntity) tileEntity).getHeldItem();
				if(!stack.isEmpty()) {
					world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
				}
			}
			super.onRemove(state, world, pos, newState, moving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean moving) {
		boolean flag = world.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), 2);
		}
	}
}
