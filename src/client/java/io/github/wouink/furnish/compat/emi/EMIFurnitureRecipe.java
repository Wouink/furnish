package io.github.wouink.furnish.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EMIFurnitureRecipe implements EmiRecipe {
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;

    public EMIFurnitureRecipe(FurnitureRecipe recipe) {
        this.input = List.of(EmiIngredient.of(recipe.getIngredients().get(0)));
        this.output = List.of(EmiStack.of(recipe.getResultItem(null)));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return FurnishEMIPlugin.FURNITURE_MAKING;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        // https://github.com/emilyploszaj/emi/wiki/Getting-Started-Guide#non-wrapper-recipes
        String itemName = output.get(0).getItemStack().getItem().toString().split(":")[1];
        return ResourceLocation.fromNamespaceAndPath(
                Furnish.MOD_ID,
                "/furniture_making/" + itemName
        );
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 76;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgetHolder.addSlot(input.get(0), 0, 0);
        widgetHolder.addSlot(output.get(0), 58, 0).recipeContext(this);
    }
}
