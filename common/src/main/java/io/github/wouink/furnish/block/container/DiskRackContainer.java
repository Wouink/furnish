package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.tileentity.DiskRackTileEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;

public class DiskRackContainer extends AbstractContainerMenu {
	protected final Container inventory;

	private static final TagKey MUSIC_DISCS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "music_discs"));

	public static boolean canPlaceInRack(ItemStack stack) {
		return stack.getItem() instanceof RecordItem || stack.is(MUSIC_DISCS);
	}

	public DiskRackContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(DiskRackTileEntity.SIZE));
	}

	public DiskRackContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(FurnishRegistries.Disk_Rack_Container.get(), syncId);
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
	public void removed(Player playerEntity) {
		super.removed(playerEntity);
		this.inventory.stopOpen(playerEntity);
	}

	@Override
	public boolean stillValid(Player playerEntity) {
		return inventory.stillValid(playerEntity);
	}

	@Override
	public ItemStack quickMoveStack(Player playerEntity, int slotIndex) {
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
