package io.github.wouink.furnish.integration.jei;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;

@JeiPlugin
public class FurnishPlugin implements IModPlugin {
	public static final ResourceLocation RECIPE_GUI_FURNISH = new ResourceLocation(Furnish.MODID, "textures/gui/jei.png");

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Furnish.MODID, Furnish.MODID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new FurnitureMakingCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Collection<FurnitureRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FurnishData.Furniture_Recipe);
		registration.addRecipes(Arrays.asList(recipes.toArray()), FurnitureMakingCategory.FURNITURE_MAKING);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(FurnishBlocks.Furniture_Workbench), FurnitureMakingCategory.FURNITURE_MAKING);
	}
}
