package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.container.DiskRackMenu;
import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DiskRackBlockEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 8;
	private NonNullList<ItemStack> inventory;

	public DiskRackBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Disk_Rack_BlockEntity.get(), pos, state);
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	}

	@Override
	protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.loadAdditional(compoundTag, provider);
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compoundTag, inventory, provider);
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.saveAdditional(compoundTag, provider);
		ContainerHelper.saveAllItems(compoundTag, inventory, provider);
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
		return new DiskRackMenu(syncId, playerInventory, this);
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
		// update for render
		BlockEntityHelper.broadcastUpdate(this, true);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	public final NonNullList<ItemStack> getItemsForRender() {
		return inventory;
	}

	// communication between client/server for rendering purposes

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveWithoutMetadata(provider);
	}
}
