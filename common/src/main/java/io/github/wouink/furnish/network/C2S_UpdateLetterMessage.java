package io.github.wouink.furnish.network;

import com.google.common.base.Charsets;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2S_UpdateLetterMessage implements CustomPacketPayload {
    public static final Type TYPE = new Type(ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "c2s_update_letter"));
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private int slot;
    private int newContentLength;
    private String newContent;
    public C2S_UpdateLetterMessage(int _slot, String _newContent) {
        this.slot = _slot;
        this.newContentLength = _newContent.length();
        this.newContent = _newContent;
    }

    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(newContentLength);
        buf.writeCharSequence(newContent, Charsets.UTF_8);
    }

    public static C2S_UpdateLetterMessage decode(RegistryFriendlyByteBuf buf) {
        int slot = buf.readInt();
        int length = buf.readInt();
        String content = buf.readCharSequence(length, Charsets.UTF_8).toString();
        return new C2S_UpdateLetterMessage(slot, content);
    }

    public static void handle(ServerPlayer player, C2S_UpdateLetterMessage message) {
        player.getInventory().getItem(message.slot).set(FurnishRegistries.Letter_Text.get(), message.newContent);
        player.getInventory().setChanged();
    }
}
