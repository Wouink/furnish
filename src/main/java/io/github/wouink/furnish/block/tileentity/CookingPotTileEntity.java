package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.container.CookingPotContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class CookingPotTileEntity extends LockableLootTileEntity {
	public static final int SIZE = 9;
	private static final Random RANDOM = new Random();
	private NonNullList<ItemStack> inventory;

	protected CookingPotTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CookingPotTileEntity() {
		this(FurnishManager.TileEntities.Cooking_Pot.get());
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
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		ItemStackHelper.saveAllItems(nbt, inventory);
		return nbt;
	}

	// copied from DispenserTileEntity
	public int getRandomSlot() {
		this.unpackLootTable(null);
		int i = -1;
		int j = 1;

		for(int k = 0; k < this.inventory.size(); ++k) {
			if (!this.inventory.get(k).isEmpty() && RANDOM.nextInt(j++) == 0) {
				i = k;
			}
		}

		return i;
	}

	public ItemStack pop() {
		int slot = getRandomSlot();
		if(slot == -1) return ItemStack.EMPTY;
		ItemStack out = inventory.get(slot).copy();
		ItemStack inside = inventory.get(slot).copy();
		inside.setCount(inside.getCount() - 1);
		inventory.set(slot, inside);
		out.setCount(1);
		return out;
	}

	public ItemStack popSimilar(ItemStack stack) {
		if(stack.getCount() == stack.getMaxStackSize()) return stack;
		for(int i = 0; i < inventory.size(); i++) {
			if(inventory.get(i).sameItem(stack)) {
				ItemStack invStack = inventory.get(i);
				invStack.setCount(invStack.getCount() - 1);
				inventory.set(i, invStack);
				stack.setCount(stack.getCount() + 1);
				return stack;
			}
		}
		return stack;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new CookingPotContainer(syncId, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}
}
