package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public record SendRecipesS2C(List<RecipeHolder<FurnitureRecipe>> recipes) implements CustomPacketPayload {
    private static final Identifier ID = Identifier.fromNamespaceAndPath(Furnish.MOD_ID, "send_recipes");
    public static final CustomPacketPayload.Type TYPE = new CustomPacketPayload.Type(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SendRecipesS2C> CODEC = StreamCodec.of(
            (buffer, msg) -> buffer.writeCollection(msg.recipes, (buf, holder) -> RecipeHolder.STREAM_CODEC.encode(buffer, holder)),
            buffer -> new SendRecipesS2C(buffer.readList(buf -> (RecipeHolder<FurnitureRecipe>) RecipeHolder.STREAM_CODEC.decode(buffer)))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
