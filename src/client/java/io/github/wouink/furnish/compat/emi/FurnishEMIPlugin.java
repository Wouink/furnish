package io.github.wouink.furnish.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

// https://github.com/emilyploszaj/emi/wiki/Getting-Started-Guide
public class FurnishEMIPlugin implements EmiPlugin {
    public static final ResourceLocation FURNITURE_RECIPE = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "furniture_making");
    public static final ResourceLocation FURNITURE_MAKING_GUI = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "textures/gui/emi_icon.png");
    public static final EmiStack FURNITURE_WORKBENCH = EmiStack.of(FurnishContents.FURNITURE_WORKBENCH);
    public static final EmiRecipeCategory FURNITURE_MAKING = new EmiRecipeCategory(
            FURNITURE_RECIPE,
            FURNITURE_WORKBENCH,
            new EmiTexture(FURNITURE_MAKING_GUI, 0, 0, 16, 16)
    );

    @Override
    public void register(EmiRegistry emiRegistry) {
        emiRegistry.addCategory(FURNITURE_MAKING);
        emiRegistry.addWorkstation(FURNITURE_MAKING, FURNITURE_WORKBENCH);

        RecipeManager manager = emiRegistry.getRecipeManager();
        for(RecipeHolder<FurnitureRecipe> holder : manager.getAllRecipesFor(FurnishContents.FURNITURE_RECIPE)) {
            emiRegistry.addRecipe(new EMIFurnitureRecipe(holder.value()));
        }
    }
}
