package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
import io.github.wouink.furnish.block.util.IFurnitureWithSound;
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

public class WideInventoryFurniture extends WideFurniture implements ISidedInventoryProvider, IFurnitureWithSound {
	private final RegistryObject<SoundEvent> openSound;
	private final RegistryObject<SoundEvent> closeSound;
	public WideInventoryFurniture(Properties p, final RegistryObject<SoundEvent> openSound, final RegistryObject<SoundEvent> closeSound) {
		super(p);
		this.openSound = openSound;
		this.closeSound = closeSound;
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
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else {
			TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
			if(tileEntity instanceof LargeFurnitureTileEntity) {
				playerEntity.openMenu((INamedContainerProvider) tileEntity);
			}
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
		TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
		if(tileEntity instanceof ISidedInventoryProvider) {
			return (ISidedInventory) tileEntity;
		}
		return null;
	}

	@Override
	public SoundEvent getOpenSound() {
		return openSound.get();
	}

	@Override
	public SoundEvent getCloseSound() {
		return closeSound.get();
	}
}
