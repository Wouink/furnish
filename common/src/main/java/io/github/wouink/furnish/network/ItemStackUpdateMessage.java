package io.github.wouink.furnish.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ItemStackUpdateMessage {

	public static final int MESSAGE_ID = 1;

	private int slot;
	private ItemStack stack;

	public ItemStackUpdateMessage(int slot, ItemStack stack) {
		this.slot = slot;
		this.stack = stack;
	}

	public int getSlot() {
		return slot;
	}

	public ItemStack getStack() {
		return stack;
	}

	public void encode(FriendlyByteBuf packetBuffer) {
		packetBuffer.writeVarInt(getSlot());
		packetBuffer.writeItem(getStack());
	}

	public static ItemStackUpdateMessage decode(FriendlyByteBuf packetBuffer) {
		int decodedSlot = packetBuffer.readVarInt();
		ItemStack decodedStack = packetBuffer.readItem();
		return new ItemStackUpdateMessage(decodedSlot, decodedStack);
	}

	public static void process(ItemStackUpdateMessage message, ServerPlayer player) {
		player.getInventory().setItem(message.getSlot(), message.getStack());
		player.getInventory().setChanged();
	}
}
