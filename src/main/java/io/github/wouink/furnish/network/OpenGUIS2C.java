package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenGUIS2C(Integer guiId) implements CustomPacketPayload {
    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "open_gui");
    public static final Type TYPE = new Type(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenGUIS2C> CODEC = StreamCodec.composite(ByteBufCodecs.INT, OpenGUIS2C::guiId, OpenGUIS2C::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
