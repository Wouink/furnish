package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.block.container.DiskRackContainer;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DiskRackTileEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 8;
	private NonNullList<ItemStack> inventory;

	public DiskRackTileEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Disk_Rack_BlockEntity.get(), pos, state);
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, inventory);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, inventory);
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
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
	public void setItem(int slot, ItemStack stack) {
		super.setItem(slot, stack);
		// update for render
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
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
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	/*
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
	 */
}
