package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.container.CrateContainer;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

public class CrateBlockEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 9;
	private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
	private NonNullList<ItemStack> inventory;

	public CrateBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Crate_BlockEntity.get(), pos, state);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> newInventory) {
		inventory = newInventory;
	}

	@Override
	public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.saveAdditional(nbt, provider);
		if(!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, inventory, provider);
		}
	}

	@Override
	protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
		super.loadAdditional(nbt, provider);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if(!this.tryLoadLootTable(nbt) && nbt.contains("Items", 9)) {
			ContainerHelper.loadAllItems(nbt, inventory, provider);
		}
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new CrateContainer(syncId, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}
}
