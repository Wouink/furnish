package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.tileentity.BookshelfChestBlockEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class BookshelfChestContainer extends ConditionalSlotContainer {
	public static boolean canPlace(ItemStack stack) {
		return stack.is(FurnishRegistries.BOOKS_TAG);
	}
	public BookshelfChestContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(BookshelfChestBlockEntity.SIZE));
	}
	public BookshelfChestContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(1, BookshelfChestContainer::canPlace, FurnishRegistries.Bookshelf_Chest_Container.get(), syncId, playerInventory, inventory);
	}
}
