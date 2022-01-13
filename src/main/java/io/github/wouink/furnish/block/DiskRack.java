package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.DiskRackTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DiskRack extends HorizontalBlock {
	private static final VoxelShape RACK_X = Block.box(0, 0, 4, 16, 2, 12);
	private static final VoxelShape RACK_Z = Block.box(4, 0, 0, 12, 2, 16);

	public DiskRack(Properties p) {
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
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? RACK_X : RACK_Z;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new DiskRackTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof DiskRackTileEntity) {
				playerEntity.openMenu((INamedContainerProvider) tileEntity);
			}
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof IInventory) {
				InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
