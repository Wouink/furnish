package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.RecycleBin;
import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RecycleBinBlockEntity extends FurnishInventoryBlockEntity {
	public RecycleBinBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(FurnishRegistries.Recycle_Bin_BlockEntity.get(), blockPos, blockState);
	}

	@Override
	public int getCapacity() {
		return 9;
	}

	@Override
	public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
		return new ChestMenu(MenuType.GENERIC_3x3, syncId, playerInventory, this, 1);
	}

	@Override
	public boolean broadcastInventoryUpdates() {
		return true;
	}

	@Override
	public boolean broadcastInventoryUpdatesToRedstone() {
		return true;
	}

	public void empty() {
		for(int slot = 0; slot < getContainerSize(); slot++) {
			// if any item is found, play this recycle bin's empty sound
			if(!getItem(slot).isEmpty()) {
				this.level.playSound(null, getBlockPos(), ((RecycleBin)getBlockState().getBlock()).getSound().get(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
			}
			break;
		}
		setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
		BlockEntityHelper.broadcastUpdate(this, true);
	}

	public ItemStack addItem(ItemStack stack) {
		ItemStack ret = stack;
		for(int i = 0; i < getContainerSize(); i++) {
			if(getItem(i).isEmpty()) {
				setItem(i, stack);
				ret = ItemStack.EMPTY;
				break;
			}
		}
		return ret;
	}
}
