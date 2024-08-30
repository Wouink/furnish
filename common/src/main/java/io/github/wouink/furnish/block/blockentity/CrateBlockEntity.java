package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.container.CrateMenu;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrateBlockEntity extends FurnishInventoryBlockEntity {
	public static final int SIZE = 9;

	public CrateBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(FurnishRegistries.Crate_BlockEntity.get(), blockPos, blockState);
	}

	@Override
	public int getCapacity() {
		return SIZE;
	}

	@Override
	public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
		return new CrateMenu(syncId, playerInventory, this);
	}

	@Override
	public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
		return CrateMenu.canPlaceInCrate(itemStack);
	}
}
