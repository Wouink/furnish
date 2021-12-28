package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.CrateContainer;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class CrateTileEntity extends LockableLootTileEntity implements ISidedInventory {
	public static final int SIZE = 9;
	private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
	private NonNullList<ItemStack> inventory;
	protected CrateTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CrateTileEntity() {
		super(FurnishData.TileEntities.TE_Crate.get());
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
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		return saveToTag(nbt);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		loadFromTag(nbt);
	}

	public CompoundNBT saveToTag(CompoundNBT nbt) {
		if(!this.tryLoadLootTable(nbt)) {
			ItemStackHelper.saveAllItems(nbt, this.inventory, false);
		}
		return nbt;
	}

	public void loadFromTag(CompoundNBT nbt) {
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if(!this.tryLoadLootTable(nbt) && nbt.contains("Items", 9)) {
			ItemStackHelper.loadAllItems(nbt, inventory);
		}
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new CrateContainer(syncId, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	public int[] getSlotsForFace(Direction dir) {
		return SLOTS;
	}

	@Override
	public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
		return CrateContainer.canPlaceInCrate(stack);
	}

	@Override
	public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
		return true;
	}
}
