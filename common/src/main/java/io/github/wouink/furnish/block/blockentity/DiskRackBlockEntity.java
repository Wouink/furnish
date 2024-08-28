package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.container.DiskRackMenu;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class DiskRackBlockEntity extends FurnishInventoryBlockEntity {
	public static final int SIZE = 8;

	public DiskRackBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(FurnishRegistries.Disk_Rack_BlockEntity.get(), blockPos, blockState);
	}

	@Override
	public int getCapacity() {
		return SIZE;
	}

	@Override
	public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
		return new DiskRackMenu(syncId, playerInventory, this);
	}

	@Override
	public boolean broadcastInventoryUpdates() {
		return true;
	}

	@Override
	public boolean broadcastInventoryUpdatesToRedstone() {
		return true;
	}
}
