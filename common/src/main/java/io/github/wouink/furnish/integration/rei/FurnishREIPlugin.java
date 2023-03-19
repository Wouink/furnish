package io.github.wouink.furnish.integration.rei;

import io.github.wouink.furnish.Furnish;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class FurnishREIPlugin implements REIServerPlugin {
    public static final CategoryIdentifier<FurnitureRecipeDisplay> FURNITURE_MAKING = CategoryIdentifier.of(Furnish.MODID, "furniture_making");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(FURNITURE_MAKING, FurnitureRecipeDisplay.serializer());
    }
}
