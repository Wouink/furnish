package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.tileentity.CrateTileEntity;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CrateContainer extends ConditionalSlotContainer {

	private static final TagKey CRATE_BLACKLIST = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "crate_blacklist"));

	public static boolean canPlaceInCrate(ItemStack stack) {
		return !stack.is(CRATE_BLACKLIST);
	}

	public CrateContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(CrateTileEntity.SIZE));
	}

	public CrateContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(1, CrateContainer::canPlaceInCrate, FurnishData.Containers.Crate.get(), syncId, playerInventory, inventory);
	}
}
