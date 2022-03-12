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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collection;

@JeiPlugin
public class FurnishJEIPlugin implements IModPlugin {
	private static final ResourceLocation UID = new ResourceLocation(Furnish.MODID, "jei");
	public static final ResourceLocation JEI_UI = new ResourceLocation(Furnish.MODID, "textures/gui/jei.png");

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IModPlugin.super.registerCategories(registration);
		registration.addRecipeCategories(new FurnitureRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		IModPlugin.super.registerRecipes(registration);
		Collection<FurnitureRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(FurnishData.Furniture_Recipe);
		registration.addRecipes(Arrays.asList(recipes.toArray()), FurnishData.Furniture_Recipe_Loc);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		IModPlugin.super.registerRecipeCatalysts(registration);
		registration.addRecipeCatalyst(new ItemStack(FurnishBlocks.Furniture_Workbench.get()), FurnishData.Furniture_Recipe_Loc);
	}
}
