package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.blockentity.BookshelfChestBlockEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BookshelfChestMenu extends ConditionalSlotMenu {
	public static boolean canPlace(ItemStack stack) {
		return stack.is(FurnishRegistries.BOOKS_TAG);
	}
	public BookshelfChestMenu(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(BookshelfChestBlockEntity.SIZE));
	}
	public BookshelfChestMenu(int syncId, Inventory playerInventory, Container inventory) {
		super(1, BookshelfChestMenu::canPlace, FurnishRegistries.Bookshelf_Chest_Container.get(), syncId, playerInventory, inventory);
	}
}
