package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

public class LargeFurnitureBlockEntity extends FurnishInventoryBlockEntity {

	public LargeFurnitureBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(FurnishRegistries.Large_Furniture_BlockEntity.get(), blockPos, blockState);
	}

	@Override
	public int getCapacity() {
		return 54;
	}

	@Override
	public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
		return new ChestMenu(MenuType.GENERIC_9x6, syncId, playerInventory, this, 6);
	}
}

