package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.tileentity.DiskRackTileEntity;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.ResourceLocation;

public class DiskRackContainer extends Container {
	protected final IInventory inventory;

	private static final ResourceLocation MUSIC_DISCS = new ResourceLocation("furnish", "music_discs");

	public static boolean canPlaceInRack(ItemStack stack) {
		return stack.getItem() instanceof MusicDiscItem || stack.getItem().getTags().contains(MUSIC_DISCS);
	}

	public DiskRackContainer(int syncId, PlayerInventory playerInventory, IInventory inventory) {
		super(FurnishData.Containers.Disk_Rack.get(), syncId);
		this.inventory = inventory;
		this.inventory.startOpen(playerInventory.player);

		// inventory
		for(int ind = 0; ind < 8; ind++) {
			this.addSlot(new ConditionalSlot(DiskRackContainer::canPlaceInRack, inventory, ind, 17 + ind * 18, 18));
		}

		// i = (#rows - #playersRows) * 18
		int i = -3 * 18;

		// player's inventory
		for(int l = 0; l < 3; ++l) {
			for(int j1 = 0; j1 < 9; ++j1) {
				this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
			}
		}

		// player's hotbar
		for(int i1 = 0; i1 < 9; ++i1) {
			this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 + i));
		}
	}

	@Override
	public void removed(PlayerEntity playerEntity) {
		super.removed(playerEntity);
		this.inventory.stopOpen(playerEntity);
	}

	public DiskRackContainer(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new Inventory(DiskRackTileEntity.SIZE));
	}

	@Override
	public boolean stillValid(PlayerEntity playerEntity) {
		return inventory.stillValid(playerEntity);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity playerEntity, int slotIndex) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.slots.get(slotIndex);

		if (slot != null && slot.hasItem()) {
			ItemStack stackInSlot = slot.getItem();
			stack = stackInSlot.copy();

			if (slotIndex < this.inventory.getContainerSize()) {
				if (!this.moveItemStackTo(stackInSlot, this.inventory.getContainerSize(), this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.moveItemStackTo(stackInSlot, 0, this.inventory.getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}

			if (stackInSlot.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
		}

		return stack;
	}
}
