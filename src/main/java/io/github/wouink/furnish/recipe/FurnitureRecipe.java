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
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.StonecutterRecipeDisplay;
import net.minecraft.world.level.Level;

import java.util.List;

public class FurnitureRecipe extends SingleItemRecipe {
    public static final MapCodec<FurnitureRecipe> MAP_CODEC = simpleMapCodec(FurnitureRecipe::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, FurnitureRecipe> STREAM_CODEC = simpleStreamCodec(FurnitureRecipe::new);
    public static final RecipeSerializer<FurnitureRecipe> SERIALIZER;

    public FurnitureRecipe(final Recipe.CommonInfo commonInfo, final Ingredient ingredient, final ItemStackTemplate result) {
        super(commonInfo, ingredient, result);
    }

    public RecipeType<FurnitureRecipe> getType() {
        return FurnishContents.FURNITURE_RECIPE;
    }

    public RecipeSerializer<FurnitureRecipe> getSerializer() {
        return SERIALIZER;
    }

    public String group() {
        return "";
    }

    public List<RecipeDisplay> display() {
        return List.of(new StonecutterRecipeDisplay(this.input().display(), this.resultDisplay(), new SlotDisplay.ItemSlotDisplay(Items.STONECUTTER)));
    }

    public SlotDisplay resultDisplay() {
        return new SlotDisplay.ItemStackSlotDisplay(this.result());
    }

    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.STONECUTTER;
    }

    static {
        SERIALIZER = new RecipeSerializer(MAP_CODEC, STREAM_CODEC);
    }
}
