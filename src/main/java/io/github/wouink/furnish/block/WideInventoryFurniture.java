package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class WideInventoryFurniture extends WideFurniture implements ISidedInventoryProvider {
	private final RegistryObject<SoundEvent> sound;
	public WideInventoryFurniture(Properties p, String registryName, final RegistryObject<SoundEvent> sound) {
		super(p, registryName);
		this.sound = sound;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
		if (tileEntity instanceof IInventory) {
			InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return !state.getValue(RIGHT).booleanValue();
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		if(!state.getValue(RIGHT).booleanValue()) return new LargeFurnitureTileEntity();
		return null;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
		if(tileEntity instanceof LargeFurnitureTileEntity) {
			if(sound != null) world.playSound(playerEntity, pos, sound.get(), SoundCategory.BLOCKS, .8f, 1.0f);
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
	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
		TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
		if(tileEntity instanceof ISidedInventoryProvider) {
			return (ISidedInventory) tileEntity;
		}
		return null;
	}
}
