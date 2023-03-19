package io.github.wouink.furnish.integration.rei.client;

import io.github.wouink.furnish.integration.rei.FurnishREIPlugin;
import io.github.wouink.furnish.integration.rei.FurnitureRecipeDisplay;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.impl.ClientInternals;

import java.util.function.Supplier;

public class FurnishREIPluginClient implements REIClientPlugin {
    public FurnishREIPluginClient() {
        ClientInternals.attachInstance((Supplier<Object>) () -> this, "furnish_rei_client_plugin");
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new FurnitureRecipeCategory());
        registry.addWorkstations(FurnishREIPlugin.FURNITURE_MAKING, EntryStacks.of(FurnishBlocks.Furniture_Workbench.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(FurnitureRecipe.class, FurnishRegistries.Furniture_Recipe.get(), FurnitureRecipeDisplay::new);
    }
}
