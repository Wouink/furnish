package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.tileentity.AmphoraTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ISidedInventoryProvider;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Amphora extends SimpleFurniture implements ISidedInventoryProvider {
	public static final VoxelShape AMPHORA_BODY = Block.box(2, 0, 2, 14, 13, 14);
	public static final VoxelShape AMPHORA_TOP = Block.box(4, 13, 4, 12, 16, 12);
	public static final VoxelShape AMPHORA = VoxelShapes.or(AMPHORA_BODY, AMPHORA_TOP).optimize();
	public Amphora(Properties p, String registryName) {
		super(p, registryName);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return AMPHORA;
	}

	@Override
	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof ISidedInventoryProvider) {
			return (ISidedInventory) tileEntity;
		}
		return null;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AmphoraTileEntity();
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof AmphoraTileEntity) {
			world.playSound(playerEntity, pos, FurnishManager.Sounds.Open_Furniture.get(), SoundCategory.BLOCKS, .8f, 1.0f);
			if(world.isClientSide()) {
				return ActionResultType.SUCCESS;
			} else {
				playerEntity.openMenu((INamedContainerProvider) tileEntity);
				return ActionResultType.CONSUME;
			}
		}
		return ActionResultType.FAIL;
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
