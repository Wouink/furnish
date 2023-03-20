package io.github.wouink.furnish.integration.jei;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FurnitureRecipeCategory implements IRecipeCategory<FurnitureRecipe> {
    protected static final RecipeType<FurnitureRecipe> FURNITURE_RECIPE_RECIPE_TYPE = RecipeType.create(Furnish.MODID, "furniture_making", FurnitureRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    protected FurnitureRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(FurnishJEIPlugin.ALL_UI_COMPONENTS, 0, 220, 82, 34);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FurnishBlocks.Furniture_Workbench.get()));
    }
    @Override
    public RecipeType<FurnitureRecipe> getRecipeType() {
        return FURNITURE_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("furnish.furniture_making");
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, FurnitureRecipe furnitureRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(furnitureRecipe.getIngredients().get(0));
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(furnitureRecipe.getResultItem());
    }
}
