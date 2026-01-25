package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record OpenGUIS2C(ItemStack source) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "open_gui");
    public static final Type TYPE = new Type(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGUIS2C> CODEC = StreamCodec.composite(ItemStack.STREAM_CODEC, OpenGUIS2C::source, OpenGUIS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
