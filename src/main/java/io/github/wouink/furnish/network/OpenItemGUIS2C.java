package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public record OpenItemGUIS2C(int slot, ItemStack source) implements CustomPacketPayload {
    private static final Identifier ID = Identifier.fromNamespaceAndPath(Furnish.MOD_ID, "open_gui");
    public static final Type TYPE = new Type(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenItemGUIS2C> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, OpenItemGUIS2C::slot,
            ItemStack.STREAM_CODEC, OpenItemGUIS2C::source,
            OpenItemGUIS2C::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
