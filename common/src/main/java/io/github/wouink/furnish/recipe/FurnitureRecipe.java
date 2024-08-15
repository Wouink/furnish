package io.github.wouink.furnish.recipe;

import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class FurnitureRecipe extends SingleItemRecipe {
	public FurnitureRecipe(String group, Ingredient ing, ItemStack stack) {
		super(FurnishRegistries.Furniture_Recipe.get(), FurnishRegistries.Furniture_Recipe_Serializer.get(), group, ing, stack);
	}
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(FurnishBlocks.Furniture_Workbench.get());
	}

	@Override
	public boolean matches(SingleRecipeInput recipeInput, Level level) {
		return this.ingredient.test(recipeInput.item());
	}

	// set ignored by recipe book
	// prevents error messages when joining world
	@Override
	public boolean isSpecial() {
		return true;
	}
}
