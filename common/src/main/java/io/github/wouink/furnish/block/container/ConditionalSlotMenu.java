package io.github.wouink.furnish.block.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ConditionalSlotMenu extends AbstractContainerMenu {
	protected final Container inventory;
	private final int rows;

	public ConditionalSlotMenu(int rows, Predicate<ItemStack> condition, MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
		super(type, syncId);
		this.inventory = inventory;
		this.rows = rows;
		inventory.startOpen(playerInventory.player);

		int i = (this.rows - 4) * 18;

		// container's inventory
		for(int j = 0; j < this.rows; ++j) {
			for(int k = 0; k < 9; ++k) {
				this.addSlot(new ConditionalSlot(condition, inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
			}
		}

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
		inventory.stopOpen(playerEntity);
	}

	public int getRowCount() {
		return this.rows;
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
