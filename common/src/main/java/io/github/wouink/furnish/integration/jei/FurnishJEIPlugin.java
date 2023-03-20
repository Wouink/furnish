package io.github.wouink.furnish.integration.jei;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@JeiPlugin
public class FurnishJEIPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(Furnish.MODID, "jei");
    protected static final ResourceLocation ALL_UI_COMPONENTS = new ResourceLocation(Furnish.MODID, "textures/gui/jei.png");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        registration.addRecipeCategories(new FurnitureRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        Optional.ofNullable(Minecraft.getInstance().level).ifPresent(
                clientLevel -> {
                    registration.addRecipes(FurnitureRecipeCategory.FURNITURE_RECIPE_RECIPE_TYPE, clientLevel.getRecipeManager().getAllRecipesFor(FurnishRegistries.Furniture_Recipe.get()));
                }
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        IModPlugin.super.registerRecipeCatalysts(registration);
        registration.addRecipeCatalyst(new ItemStack(FurnishBlocks.Furniture_Workbench.get()), FurnitureRecipeCategory.FURNITURE_RECIPE_RECIPE_TYPE);
    }
}
