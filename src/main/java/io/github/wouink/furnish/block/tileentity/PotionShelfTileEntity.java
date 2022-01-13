package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.PotionShelf;
import io.github.wouink.furnish.block.container.PotionShelfContainer;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class PotionShelfTileEntity extends LockableLootTileEntity {
	public static final int SIZE = 4;
	private NonNullList<ItemStack> inventory;

	public PotionShelfTileEntity() {
		super(FurnishData.TileEntities.TE_Potion_Shelf.get());
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new PotionShelfContainer(syncId, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		ItemStackHelper.saveAllItems(nbt, inventory);
		return nbt;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> newInv) {
		inventory = newInv;
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		switch(slot) {
			case 0:
				level.setBlock(worldPosition, getBlockState().setValue(PotionShelf.TOP_LEFT, !stack.isEmpty()), 2);
				break;
			case 1:
				level.setBlock(worldPosition, getBlockState().setValue(PotionShelf.TOP_RIGHT, !stack.isEmpty()), 2);
				break;
			case 2:
				level.setBlock(worldPosition, getBlockState().setValue(PotionShelf.BOTTOM_LEFT, !stack.isEmpty()), 2);
				break;
			case 3:
				level.setBlock(worldPosition, getBlockState().setValue(PotionShelf.BOTTOM_RIGHT, !stack.isEmpty()), 2);
				break;
		}
	}

	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 1239, save(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState state = this.getBlockState();
		load(state, pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		load(state, tag);
	}
}
