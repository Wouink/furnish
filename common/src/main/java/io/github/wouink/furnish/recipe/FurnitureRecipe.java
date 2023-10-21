package io.github.wouink.furnish.recipe;

import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class FurnitureRecipe extends FSingleItemRecipe {
	public FurnitureRecipe(ResourceLocation rl, String s, Ingredient ing, ItemStack stack) {
		super(FurnishRegistries.Furniture_Recipe.get(), FurnishRegistries.Furniture_Recipe_Serializer.get(), rl, s, ing, stack);
	}

	@Override
	public boolean matches(Container inv, Level l) {
		return this.ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(FurnishBlocks.Furniture_Workbench.get());
	}

	// set ignored by recipe book
	// prevents error messages when joining world
	@Override
	public boolean isSpecial() {
		return true;
	}
}
