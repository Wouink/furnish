package io.github.wouink.furnish.integration.jei;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FurnitureMakingCategory implements IRecipeCategory<FurnitureRecipe> {
	public static final ResourceLocation FURNITURE_MAKING = new ResourceLocation(Furnish.MODID, "furniture_making");

	private final IDrawable background;
	private final IDrawable icon;

	private static final int inputSlot = 0;
	private static final int outputSlot = 1;

	public static final int width = 82;
	public static final int height = 34;

	public FurnitureMakingCategory(IGuiHelper guiHelper) {
		ResourceLocation loc = FurnishPlugin.RECIPE_GUI_FURNISH;
		background = guiHelper.createDrawable(loc, 0, 220, width, height);
		icon = guiHelper.createDrawableIngredient(new ItemStack(FurnishBlocks.Furniture_Workbench));
	}

	@Override
	public ResourceLocation getUid() {
		return FURNITURE_MAKING;
	}

	@Override
	public Class<? extends FurnitureRecipe> getRecipeClass() {
		return FurnitureRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.get("furnish.furniture_making");
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
		IGuiItemStackGroup guiItemStackGroup = recipeLayout.getItemStacks();
		guiItemStackGroup.init(inputSlot, true, 0, 8);
		guiItemStackGroup.init(outputSlot, false, 60, 8);
		guiItemStackGroup.set(ingredients);
	}
}
