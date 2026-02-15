package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public record UpdateLetterC2S(int slot, String text, Optional<String> author) implements CustomPacketPayload {
    private static final Identifier ID = Identifier.fromNamespaceAndPath(Furnish.MOD_ID, "update_letter");
    public static final Type TYPE = new Type(ID);
    public static final StreamCodec<ByteBuf, UpdateLetterC2S> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateLetterC2S::slot,
            ByteBufCodecs.STRING_UTF8, UpdateLetterC2S::text,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional), UpdateLetterC2S::author,
            UpdateLetterC2S::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
