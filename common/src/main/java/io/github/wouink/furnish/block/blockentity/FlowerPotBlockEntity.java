package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.FlowerPot;
import io.github.wouink.furnish.block.util.TileEntityHelper;
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

public class FlowerPotBlockEntity extends RandomizableContainerBlockEntity {

	protected NonNullList<ItemStack> inventory;

	public FlowerPotBlockEntity(BlockPos p_155630_, BlockState p_155631_) {
		super(FurnishRegistries.Flower_Pot_BlockEntity.get(), p_155630_, p_155631_);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		inventory = stacks;
	}

	@Override
	protected Component getDefaultName() {
		return null;
	}

	@Override
	protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
		// this BlockEntity is not intended to be opened in an inventory
		return null;
	}

	@Override
	public int getContainerSize() {
		return ((FlowerPot)this.getBlockState().getBlock()).getPlants();
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.saveAdditional(compoundTag, provider);
		if(!this.trySaveLootTable(compoundTag)) ContainerHelper.saveAllItems(compoundTag, inventory, provider);
	}

	@Override
	protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.loadAdditional(compoundTag, provider);
		if(!this.tryLoadLootTable(compoundTag)) ContainerHelper.loadAllItems(compoundTag, inventory, provider);
	}

	public ItemStack setPlant(int slot, ItemStack stack) {
		ItemStack ret = ItemStack.EMPTY;
		if(stack.is(FurnishRegistries.PLANTS_TAG) || stack.isEmpty()) {
			ret = inventory.get(slot);
			inventory.set(slot, stack);
		}
		setItems(inventory);
		TileEntityHelper.broadcastUpdate(this, true);
		return ret;
	}

	public final NonNullList<ItemStack> getItemsForRender() {
		return getItems();
	}

	// communication between client/server for rendering purposes

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = this.saveWithoutMetadata();
		return tag;
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
