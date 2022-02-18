package io.github.wouink.furnish.integration.jei;

import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishData;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class FurnitureRecipeCategory implements IRecipeCategory<FurnitureRecipe> {
	private final IDrawable background;
	private final IDrawable icon;

	private final int Input_Slot = 0;
	private final int Output_Slot = 1;

	public FurnitureRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(FurnishJEIPlugin.JEI_UI, 0, 220, 82, 34);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FurnishBlocks.Furniture_Workbench));
	}

	@Override
	public ResourceLocation getUid() {
		return FurnishData.Furniture_Recipe_Loc;
	}

	@Override
	public Class<? extends FurnitureRecipe> getRecipeClass() {
		return FurnitureRecipe.class;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("furnish.furniture_making");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(FurnitureRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FurnitureRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
		stackGroup.init(Input_Slot, true, 0, 8);
		stackGroup.init(Output_Slot, false, 60, 8);
		stackGroup.set(ingredients);
	}
}
