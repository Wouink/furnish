package io.github.wouink.furnish.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FurnitureRecipe extends SingleItemRecipe {
    public static final RecipeSerializer<FurnitureRecipe> SERIALIZER = new FurnitureRecipe.Serializer();

    private final String group;
    private final Ingredient ingredient;
    private final ItemStack result;

    public FurnitureRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<? extends SingleItemRecipe> getType() {
        return FurnishContents.FURNITURE_RECIPE;
    }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return this.ingredient.test(recipeInput.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput recipeInput, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public boolean isSpecial() {
        // hide in recipe book
        return true;
    }

    // with help from https://github.com/MehVahdJukaar/sawmill
    public static class Serializer implements RecipeSerializer<FurnitureRecipe> {
        private final MapCodec<FurnitureRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, FurnitureRecipe> streamCodec;

        public Serializer() {
            this.codec = RecordCodecBuilder.mapCodec(
                    furnitureRecipeInstance -> furnitureRecipeInstance.group(
                            Codec.STRING.optionalFieldOf("group", "").forGetter(arg -> arg.group),
                            Ingredient.CODEC.fieldOf("ingredient").forGetter(arg -> arg.ingredient),
                            ItemStack.STRICT_CODEC.fieldOf("result").forGetter(arg -> arg.result)
                    ).apply(furnitureRecipeInstance, FurnitureRecipe::new)
            );
            this.streamCodec = StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, arg -> arg.group,
                    Ingredient.CONTENTS_STREAM_CODEC, arg -> arg.ingredient,
                    ItemStack.STREAM_CODEC, arg -> arg.result,
                    FurnitureRecipe::new
            );
        }

        @Override
        public MapCodec<FurnitureRecipe> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FurnitureRecipe> streamCodec() {
            return streamCodec;
        }
    }

}
