package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.blockentity.CrateBlockEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateContainer extends ConditionalSlotContainer {
	public static boolean canPlaceInCrate(ItemStack stack) {
		return !stack.is(FurnishRegistries.CRATE_BLACKLIST_TAG);
	}

	public CrateContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(CrateBlockEntity.SIZE));
	}

	public CrateContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(1, CrateContainer::canPlaceInCrate, FurnishRegistries.Crate_Container.get(), syncId, playerInventory, inventory);
	}
}
