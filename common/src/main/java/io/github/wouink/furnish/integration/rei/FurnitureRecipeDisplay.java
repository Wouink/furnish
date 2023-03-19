package io.github.wouink.furnish.integration.rei;

import io.github.wouink.furnish.recipe.FurnitureRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FurnitureRecipeDisplay extends BasicDisplay {
    public FurnitureRecipeDisplay(FurnitureRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getResultItem())), Optional.ofNullable(recipe.getId()));
    }

    public FurnitureRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FurnishREIPlugin.FURNITURE_MAKING;
    }

    public static BasicDisplay.Serializer<FurnitureRecipeDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(FurnitureRecipeDisplay::new);
    }
}
