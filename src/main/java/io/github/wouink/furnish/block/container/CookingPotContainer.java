package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.tileentity.CookingPotTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CookingPotContainer extends ConditionalSlotContainer {

	private static final ResourceLocation COOKING_POT_TAG = new ResourceLocation(Furnish.MODID, "cooking_pot_whitelist");

	public static boolean canPlaceInCookingPot(ItemStack stack) {
		return stack.isEdible() || stack.getItem().getTags().contains(COOKING_POT_TAG);
	}

	public CookingPotContainer(int syncId, PlayerInventory playerInventory, IInventory inventory) {
		super(1, CookingPotContainer::canPlaceInCookingPot, FurnishManager.Containers.Cooking_Pot.get(), syncId, playerInventory, inventory);
	}

	public CookingPotContainer(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new Inventory(CookingPotTileEntity.SIZE));
	}
}
