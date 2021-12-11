package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.tileentity.CrateTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CrateContainer extends ConditionalSlotContainer{

	private static final ResourceLocation CRATE_BLACKLIST = new ResourceLocation(Furnish.MODID, "crate_blacklist");

	private static final boolean canPlaceInCrate(ItemStack stack) {
		return !stack.getItem().getTags().contains(CRATE_BLACKLIST);
	}

	public CrateContainer(int syncId, PlayerInventory playerInventory, IInventory inventory) {
		super(1, CrateContainer::canPlaceInCrate, FurnishManager.Containers.Crate.get(), syncId, playerInventory, inventory);
	}

	public CrateContainer(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new Inventory(CrateTileEntity.SIZE));
	}
}
