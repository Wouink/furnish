package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
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
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        return new RecipeProvider(provider, recipeOutput) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.MISC, FurnishContents.FURNITURE_WORKBENCH, 1)
                        .pattern("a s")
                        .pattern("ppp")
                        .pattern("lcl")
                        .define('a', Items.IRON_AXE)
                        .define('s', Items.SHEARS)
                        .define('p', ItemTags.PLANKS)
                        .define('l', ItemTags.LOGS)
                        .define('c', Items.CRAFTING_TABLE)
                        .unlockedBy(getHasName(FurnishContents.FURNITURE_WORKBENCH), has(FurnishContents.FURNITURE_WORKBENCH))
                        .save(recipeOutput);

                for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
                    Item plank = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(set.woodType.name() + "_planks")).get().value();
                    Item trapdoor = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(set.woodType.name() + "_trapdoor")).get().value();
                    String logId = set.woodType.name();
                    if(set.woodType == WoodType.CRIMSON || set.woodType == WoodType.WARPED) logId += "_stem";
                    else if(set.woodType == WoodType.BAMBOO) logId += "_block";
                    else logId += "_log";
                    Item log = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(logId)).get().value();

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
                    furnitureRecipe(recipeOutput, Ingredient.of(plank), set.wardrobe, 1);
                }

                for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
                    String color = set.dyeColor.name().toLowerCase();
                    Item terracotta = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(color + "_terracotta")).get().value();
                    Item carpet = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(color + "_carpet")).get().value();
                    Item wool = BuiltInRegistries.ITEM.get(Identifier.withDefaultNamespace(color + "_wool")).get().value();

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

                /* TODO
                furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.DISK_RACK, 1);
                furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.CHESS_BOARD, 1);
                furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.PICTURE_FRAME, 1);
                furnitureRecipe(recipeOutput, Ingredient.of(ItemTags.PLANKS), FurnishContents.RECYCLE_BIN, 1);
                 */
                furnitureRecipe(recipeOutput, Ingredient.of(Items.BOOK), FurnishContents.BOOK_PILE, 1);

                shapeless(RecipeCategory.MISC, FurnishContents.LETTER, 1)
                        .requires(Items.PAPER)
                        .requires(Items.HONEYCOMB)
                        .unlockedBy(getHasName(FurnishContents.LETTER), has(FurnishContents.LETTER))
                        .save(recipeOutput);
            }

            private void furnitureRecipe(RecipeOutput recipeOutput, Ingredient ingredient, ItemLike result, int resultCount) {
                SingleItemRecipeBuilder builder = new SingleItemRecipeBuilder(
                        RecipeCategory.DECORATIONS, FurnitureRecipe::new, ingredient, result, resultCount
                ).unlockedBy(RecipeProvider.getHasName(result), has(result));
                builder.save(recipeOutput, "furnish:furniture_making/" + RecipeProvider.getItemName(result));
            }
        };
    }

    @Override
    public String getName() {
        return "FurnishRecipesGenerator";
    }
}
