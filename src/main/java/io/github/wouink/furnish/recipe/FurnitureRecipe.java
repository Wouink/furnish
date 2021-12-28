package io.github.wouink.furnish.recipe;

import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FurnitureRecipe extends FSingleItemRecipe {
	public FurnitureRecipe(ResourceLocation rl, String s, Ingredient ing, ItemStack stack) {
		super(FurnishData.Furniture_Recipe, FurnishData.RecipeSerializers.Furniture_Recipe_Serializer.get(), rl, s, ing, stack);
	}

	@Override
	public boolean matches(IInventory inv, World w) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(FurnishBlocks.Furniture_Workbench);
	}
}
