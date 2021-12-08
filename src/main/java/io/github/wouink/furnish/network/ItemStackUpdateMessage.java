package io.github.wouink.furnish.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

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

	public void encode(PacketBuffer packetBuffer) {
		packetBuffer.writeVarInt(getSlot());
		packetBuffer.writeItem(getStack());
	}

	public static ItemStackUpdateMessage decode(PacketBuffer packetBuffer) {
		int decodedSlot = packetBuffer.readVarInt();
		ItemStack decodedStack = packetBuffer.readItem();
		return new ItemStackUpdateMessage(decodedSlot, decodedStack);
	}
}
