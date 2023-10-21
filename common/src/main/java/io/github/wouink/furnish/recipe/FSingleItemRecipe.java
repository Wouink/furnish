package io.github.wouink.furnish.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;

// should not have to write this class
// writing it due to a bug where net.minecraft.item.crafting.SingleItemRecipe.Serializer.IRecipeFactory is private and SHOULD be public

public abstract class FSingleItemRecipe implements Recipe<Container> {
	protected final Ingredient ingredient;
	protected final ItemStack result;
	private final RecipeType<?> type;
	private final RecipeSerializer<?> serializer;
	protected final ResourceLocation id;
	protected final String group;

	public FSingleItemRecipe(RecipeType<?> type, RecipeSerializer<?> serializer, ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
		this.type = type;
		this.serializer = serializer;
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = result;
	}

	public RecipeType<?> getType() {
		return this.type;
	}

	public RecipeSerializer<?> getSerializer() {
		return this.serializer;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public String getGroup() {
		return this.group;
	}

	public ItemStack getResultItem() {
		return this.result;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonNullList = NonNullList.create();
		nonNullList.add(this.ingredient);
		return nonNullList;
	}

	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	public ItemStack assemble(Container container, RegistryAccess registryAccess) {
		return this.result.copy();
	}

	public static class Serializer<T extends FSingleItemRecipe> implements RecipeSerializer<T> {
		final FSingleItemRecipe.Serializer.SingleItemMaker<T> factory;

		public Serializer(FSingleItemRecipe.Serializer.SingleItemMaker<T> factory) {
			this.factory = factory;
		}

		public T fromJson(ResourceLocation recipeId, JsonObject json) {
			String string = GsonHelper.getAsString(json, "group", "");
			Ingredient ingredient;
			if (GsonHelper.isArrayNode(json, "ingredient")) {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"), false);
			} else {
				ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"), false);
			}

			String string2 = GsonHelper.getAsString(json, "result");
			int i = GsonHelper.getAsInt(json, "count");
			ItemStack itemStack = new ItemStack((ItemLike)BuiltInRegistries.ITEM.get(new ResourceLocation(string2)), i);
			return this.factory.create(recipeId, string, ingredient, itemStack);
		}

		public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String string = buffer.readUtf();
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack itemStack = buffer.readItem();
			return this.factory.create(recipeId, string, ingredient, itemStack);
		}

		public void toNetwork(FriendlyByteBuf buffer, T recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeItem(recipe.result);
		}

		public interface SingleItemMaker<T extends FSingleItemRecipe> {
			T create(ResourceLocation resourceLocation, String string, Ingredient ingredient, ItemStack itemStack);
		}
	}
}
