package io.github.wouink.furnish.network;

import dev.architectury.networking.NetworkManager;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record ServerboundLetterUpdateMessage(int slot, String text, Optional<String> author) implements CustomPacketPayload {
    public static final Type TYPE = new Type(ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "update_letter"));
    public static final StreamCodec<ByteBuf, ServerboundLetterUpdateMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ServerboundLetterUpdateMessage::slot,
            ByteBufCodecs.STRING_UTF8,
            ServerboundLetterUpdateMessage::text,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs::optional),
            ServerboundLetterUpdateMessage::author,
            ServerboundLetterUpdateMessage::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Receiver implements NetworkManager.NetworkReceiver<ServerboundLetterUpdateMessage> {
        @Override
        public void receive(ServerboundLetterUpdateMessage message, NetworkManager.PacketContext context) {
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            player.getInventory().getItem(message.slot()).set(FurnishRegistries.Letter_Text.get(), message.text());
            message.author().ifPresent(authorName -> player.getInventory().getItem(message.slot()).set(FurnishRegistries.Letter_Author.get(), authorName));
            player.getInventory().setChanged();
        }
    }
}
