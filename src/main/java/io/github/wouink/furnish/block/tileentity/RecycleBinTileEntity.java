package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.block.RecycleBin;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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

public class RecycleBinTileEntity extends RandomizableContainerBlockEntity {
	protected NonNullList<ItemStack> inventory;

	public RecycleBinTileEntity(BlockPos pos, BlockState state) {
		super(FurnishData.TileEntities.TE_Recycle_Bin.get(), pos, state);
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
		this.level.updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
	}

	public ItemStack addItem(ItemStack stack) {
		for(int slot = 0; slot < getContainerSize(); slot++) {
			if(inventory.get(slot).isEmpty()) {
				inventory.set(slot, stack);
				this.level.updateNeighbourForOutputSignal(this.getBlockPos(), this.getBlockState().getBlock());
				return ItemStack.EMPTY;
			}
		}
		return stack;
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
	}

	@Override
	public void stopOpen(Player playerEntity) {
		// if(!playerEntity.isSpectator()) playSound(FurnishData.Sounds.Amphora_Close.get());
	}

	// copied from net.minecraft.tileentity.BarrelTileEntity
	private void playSound(SoundEvent sound) {
		double x = (double) this.worldPosition.getX() + 0.5D;
		double y = (double) this.worldPosition.getY() + 1.0D;
		double z = (double) this.worldPosition.getZ() + 0.5D;
		this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}
}
