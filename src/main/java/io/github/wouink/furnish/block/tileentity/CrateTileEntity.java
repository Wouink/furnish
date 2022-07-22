package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.block.container.CrateContainer;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
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

public class CrateTileEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 9;
	private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
	private NonNullList<ItemStack> inventory;

	public CrateTileEntity(BlockPos pos, BlockState state) {
		super(FurnishData.TileEntities.TE_Crate.get(), pos, state);
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
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if(!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, inventory, false);
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		loadFromTag(nbt);
	}

	public CompoundTag saveToTag(CompoundTag nbt) {
		if(!this.tryLoadLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, this.inventory, false);
		}
		return nbt;
	}

	public void loadFromTag(CompoundTag nbt) {
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if(!this.tryLoadLootTable(nbt) && nbt.contains("Items", 9)) {
			ContainerHelper.loadAllItems(nbt, inventory);
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
