package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.DiskRackContainer;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class DiskRackTileEntity extends LockableLootTileEntity {
	public static final int SIZE = 8;
	private NonNullList<ItemStack> inventory;

	public DiskRackTileEntity() {
		super(FurnishData.TileEntities.TE_Disk_Rack.get());
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
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
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new DiskRackContainer(syncId, playerInventory, this);
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
	public void stopOpen(PlayerEntity playerEntity) {
		super.stopOpen(playerEntity);
		// update for render
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	public final NonNullList<ItemStack> getItemsForRender() {
		return inventory;
	}

	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 1238, save(new CompoundNBT()));
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
