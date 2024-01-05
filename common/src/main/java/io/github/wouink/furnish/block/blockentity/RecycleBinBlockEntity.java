package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.RecycleBin;
import io.github.wouink.furnish.block.util.TileEntityHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RecycleBinBlockEntity extends RandomizableContainerBlockEntity {
	protected NonNullList<ItemStack> inventory;

	public RecycleBinBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Recycle_Bin_BlockEntity.get(), pos, state);
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
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if(!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, inventory);
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if(!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, inventory);
		}
	}

	public void empty() {
		for(int slot = 0; slot < getContainerSize(); slot++) {
			if(!inventory.get(slot).isEmpty()) playSound(((RecycleBin)getBlockState().getBlock()).getSound().get());
			break;
		}
		setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
		TileEntityHelper.broadcastUpdate(this, true);
	}

	public ItemStack addItem(ItemStack stack) {
		ItemStack ret = stack;
		for(int i = 0; i < getContainerSize(); i++) {
			if(inventory.get(i).isEmpty()) {
				inventory.set(i, stack);
				ret = ItemStack.EMPTY;
				break;
			}
		}
		setItems(inventory);
		TileEntityHelper.broadcastUpdate(this, true);
		return ret;
	}

	public final NonNullList<ItemStack> getItemsForRender() {
		return getItems();
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
		return new ChestMenu(MenuType.GENERIC_3x3, syncId, inv, this, 1);
	}

	@Override
	public int getContainerSize() {
		return 9;
	}

	@Override
	public void startOpen(Player playerEntity) {
		TileEntityHelper.broadcastUpdate(this, true);
	}

	@Override
	public void stopOpen(Player playerEntity) {
		TileEntityHelper.broadcastUpdate(this, true);
	}

	// copied from net.minecraft.tileentity.BarrelTileEntity
	private void playSound(SoundEvent sound) {
		double x = (double) this.worldPosition.getX() + 0.5D;
		double y = (double) this.worldPosition.getY() + 1.0D;
		double z = (double) this.worldPosition.getZ() + 0.5D;
		this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
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
