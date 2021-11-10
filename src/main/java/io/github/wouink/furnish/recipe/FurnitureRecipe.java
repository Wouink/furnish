package io.github.wouink.furnish.recipe;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FurnitureRecipe extends FSingleItemRecipe {
	public FurnitureRecipe(ResourceLocation rl, String s, Ingredient ing, ItemStack stack) {
		super(FurnishManager.RecipeType.Furniture_Recipe, FurnishManager.Serializer.Furniture.get(), rl, s, ing, stack);
	}

	@Override
	public boolean matches(IInventory inv, World w) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(FurnishManager.Furniture_Workbench);
	}
}
