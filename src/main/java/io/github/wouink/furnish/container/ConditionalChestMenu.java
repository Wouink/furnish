package io.github.wouink.furnish.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * Basically a chest, with the ability to filter the items that can go inside.
 * This container type does not need registration as it's using Minecraft's generic containers.
 */
public class ConditionalChestMenu extends AbstractContainerMenu {
    private final Container container;
    private final int containerRows;

    public static ConditionalChestMenu oneRow(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x1, syncId, inventory, container, 1, filter);
    }

    public static ConditionalChestMenu twoRows(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x2, syncId, inventory, container, 2, filter);
    }

    public static ConditionalChestMenu threeRows(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x3, syncId, inventory, container, 3, filter);
    }

    public static ConditionalChestMenu fourRows(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x4, syncId, inventory, container, 4, filter);
    }

    public static ConditionalChestMenu fiveRows(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x5, syncId, inventory, container, 5, filter);
    }

    public static ConditionalChestMenu sixRows(int syncId, Inventory inventory, Container container, Predicate<ItemStack> filter) {
        return new ConditionalChestMenu(MenuType.GENERIC_9x6, syncId, inventory, container, 6, filter);
    }

    public ConditionalChestMenu(MenuType<?> menuType, int syncId, Inventory inventory, Container container, int numRows, Predicate<ItemStack> filter) {
        super(menuType, syncId);
        checkContainerSize(container, numRows * 9);
        this.container = container;
        this.containerRows = numRows;
        container.startOpen(inventory.player);
        int k = (this.containerRows - 4) * 18;

        // container
        for(int l = 0; l < this.containerRows; ++l) {
            for(int m = 0; m < 9; ++m) {
                this.addSlot(new ConditionalSlot(filter, container, m + l * 9, 8 + m * 18, 18 + l * 18));
            }
        }

        // player's inventory
        for(int l = 0; l < 3; ++l) {
            for(int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 103 + l * 18 + k));
            }
        }

        // hotbar
        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 161 + k));
        }
    }

    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();

            if (slotIndex < this.containerRows * 9) {
                if (!this.moveItemStackTo(stackInSlot, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stackInSlot, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
