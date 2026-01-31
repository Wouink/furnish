package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.concurrent.CompletableFuture;

public class FurnishRecipesGenerator extends FabricRecipeProvider {
    public FurnishRecipesGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FurnishContents.FURNITURE_WORKBENCH, 1)
                .pattern("a s")
                .pattern("ppp")
                .pattern("lcl")
                .define('a', Items.IRON_AXE)
                .define('s', Items.SHEARS)
                .define('p', ItemTags.PLANKS)
                .define('l', ItemTags.LOGS)
                .define('c', Items.CRAFTING_TABLE);

        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            Item plank = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(set.woodType.name() + "_planks"));
            Item trapdoor = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(set.woodType.name() + "_trapdoor"));
            String logId = set.woodType.name();
            if(set.woodType == WoodType.CRIMSON || set.woodType == WoodType.WARPED) logId += "_stem";
            else if(set.woodType == WoodType.BAMBOO) logId += "_block";
            else logId += "_log";
            Item log = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(logId));

            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.squareTable, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.pedestalTable, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.table, 1);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                furnitureRecipe(recipeOutput, Ingredient.of(plank), set.bedsideTable, 1);
                furnitureRecipe(recipeOutput, Ingredient.of(plank), set.kitchenCabinet, 1);
            }

            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.cabinet, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.stool, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.chair, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.bench, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(log), set.logBench, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.ladder, 2);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.crate, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(plank), set.shelf, 2);
            furnitureRecipe(recipeOutput, Ingredient.of(trapdoor), set.shutter, 1);
        }

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            Item terracotta = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color + "_terracotta"));
            Item carpet = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color + "_carpet"));
            Item wool = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color + "_wool"));

            furnitureRecipe(recipeOutput, Ingredient.of(terracotta), set.amphora, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(carpet), set.awning, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(carpet), set.curtain, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(Items.PAPER), set.paperLamp, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(terracotta), set.plate, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(Items.GLASS), set.showcase, 1);
            furnitureRecipe(recipeOutput, Ingredient.of(wool), set.sofa, 1);
        }

        furnitureRecipe(recipeOutput, Ingredient.of(Items.TERRACOTTA), FurnishContents.PLATE, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.TERRACOTTA), FurnishContents.AMPHORA, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.IRON_BLOCK), FurnishContents.LOCKER, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.IRON_BLOCK), FurnishContents.SMALL_LOCKER, 2);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.IRON_BLOCK), FurnishContents.METAL_MAILBOX, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.IRON_BLOCK), FurnishContents.TRASH_CAN, 1);

        furnitureRecipe(recipeOutput, Ingredient.of(Items.STRING), FurnishContents.RED_BUNTING, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.STRING), FurnishContents.YELLOW_BUNTING, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.STRING), FurnishContents.GREEN_BUNTING, 1);

        furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.DISK_RACK, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.CHESS_BOARD, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.PICTURE_FRAME, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.RECYCLE_BIN, 1);
        furnitureRecipe(recipeOutput, Ingredient.of(Items.BOOK), FurnishContents.BOOK_PILE, 1);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, FurnishContents.LETTER, 3).requires(Items.PAPER, 3).requires(Items.HONEYCOMB);
    }

    private static void furnitureRecipe(RecipeOutput recipeOutput, Ingredient ingredient, ItemLike result, int resultCount) {
        SingleItemRecipeBuilder builder = new SingleItemRecipeBuilder(
                RecipeCategory.DECORATIONS, FurnitureRecipe::new, ingredient, result, resultCount
        ).unlockedBy(RecipeProvider.getHasName(result), has(result));
        builder.save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "furniture_making/" + RecipeProvider.getItemName(result)));
    }
}
