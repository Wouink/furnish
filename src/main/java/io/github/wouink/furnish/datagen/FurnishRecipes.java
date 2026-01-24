package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class FurnishRecipes extends FabricRecipeProvider {
    public FurnishRecipes(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        furnitureRecipe(recipeOutput, Ingredient.of(Items.STRING), FurnishContents.RED_BUNTING, 1);
    }

    private static void furnitureRecipe(RecipeOutput recipeOutput, Ingredient ingredient, ItemLike result, int resultCount) {
        SingleItemRecipeBuilder builder = new SingleItemRecipeBuilder(
                RecipeCategory.BUILDING_BLOCKS, FurnitureRecipe::new, ingredient, result, resultCount
        ).unlockedBy(RecipeProvider.getHasName(result), has(result));
        builder.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "furniture_making/" + RecipeProvider.getItemName(result)));
    }
}
