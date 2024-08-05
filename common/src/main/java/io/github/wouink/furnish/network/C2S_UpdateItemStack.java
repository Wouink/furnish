package io.github.wouink.furnish.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.wouink.furnish.Furnish;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

/**
 * This message informs the server about an ItemStack
 * that was modified by the client
 * @author Wouink
 */

public class C2S_UpdateItemStack extends BaseC2SMessage {
    private int slot;
    private ItemStack newStack;

    /**
     * Constructor
     * @param _slot the slot of the player's inventory containing the ItemStack
     * @param _newStack the new ItemStack (up-to-date version)
     */
    public C2S_UpdateItemStack(int _slot, ItemStack _newStack) {
        this.slot = _slot;
        this.newStack = _newStack;
    }

    public C2S_UpdateItemStack(FriendlyByteBuf buf) {
        this.slot = buf.readInt();
        this.newStack = buf.readItem();
    }

    @Override
    public MessageType getType() {
        return Furnish.CL_UPDATE_ITEMSTACK;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(slot);
        buf.writeItem(newStack);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            context.getPlayer().getInventory().setItem(slot, newStack);
            context.getPlayer().getInventory().setChanged();
        });
    }
}
