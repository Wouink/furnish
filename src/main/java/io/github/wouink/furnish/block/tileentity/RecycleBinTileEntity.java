package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.RecycleBin;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
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
import org.jetbrains.annotations.Nullable;

public class RecycleBinTileEntity extends RandomizableContainerBlockEntity {
	protected NonNullList<ItemStack> inventory;

	public RecycleBinTileEntity(BlockPos pos, BlockState state) {
		super(FurnishData.TileEntities.TE_Recycle_Bin.get(), pos, state);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		Furnish.debug("getitems");
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		Furnish.debug("setitems");
		inventory = stacks;
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		Furnish.debug("save called");
		if(!this.trySaveLootTable(nbt)) {
			Furnish.debug("container save items");
			ContainerHelper.saveAllItems(nbt, inventory);
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		Furnish.debug("load called");
		if(!this.tryLoadLootTable(nbt)) {
			Furnish.debug("container load items");
			ContainerHelper.loadAllItems(nbt, inventory);
		}
	}

	public void empty() {
		for(int slot = 0; slot < getContainerSize(); slot++) {
			if(!inventory.get(slot).isEmpty()) playSound(((RecycleBin)getBlockState().getBlock()).getSound().get());
			break;
		}
		setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
		markUpdated();
	}

	public ItemStack addItem(ItemStack stack) {
		BlockState blockStateBefore = getBlockState();
		ItemStack ret = stack;
		for(int i = 0; i < getContainerSize(); i++) {
			if(inventory.get(i).isEmpty()) {
				inventory.set(i, stack);
				ret = ItemStack.EMPTY;
				Furnish.debug("add to slot " + i);
				break;
			}
		}
		setItems(inventory);
		markUpdated();
		return ret;
	}

/*	public ItemStack addItem(ItemStack stack) {
		for(int slot = 0; slot < getContainerSize(); slot++) {
			if(inventory.get(slot).isEmpty()) {
				inventory.set(slot, stack);
				setItems(inventory);
				this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
				this.level.updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
				return ItemStack.EMPTY;
			}
		}
		this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
		return stack;
	}*/

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
		// if(!playerEntity.isSpectator()) playSound(FurnishData.Sounds.Amphora_Open.get());
		markUpdated();
	}

	@Override
	public void stopOpen(Player playerEntity) {
		// if(!playerEntity.isSpectator()) playSound(FurnishData.Sounds.Amphora_Close.get());
		markUpdated();
	}

	// copied from net.minecraft.tileentity.BarrelTileEntity
	private void playSound(SoundEvent sound) {
		double x = (double) this.worldPosition.getX() + 0.5D;
		double y = (double) this.worldPosition.getY() + 1.0D;
		double z = (double) this.worldPosition.getZ() + 0.5D;
		this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}

	private void markUpdated() {
		this.setChanged();
		this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		this.level.updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
	}

	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		Furnish.debug("get update packet");
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		Furnish.debug("on data packet");
		Furnish.debug("inventaire avant chargement = " + inventory);
		this.load(pkt.getTag());
		Furnish.debug("inventaire chargé = " + inventory);
	}

	@Override
	public CompoundTag getUpdateTag() {
		Furnish.debug("get update tag");
		Furnish.debug("inventaire actuel = " + inventory);
		CompoundTag tag = this.saveWithoutMetadata();
		Furnish.debug("inventaire envoyé = " + tag.get("Items"));
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		Furnish.debug("handle update tag");
		load(tag);
	}
}
