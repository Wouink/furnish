package io.github.wouink.furnish.network;

import io.github.wouink.furnish.Furnish;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerMessageHandler {

	public static void onMessageReceived(final ItemStackUpdateMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context ctx = contextSupplier.get();
		LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
		ctx.setPacketHandled(true);

		if(sideReceived.isClient()) {
			System.err.println("Received an ItemStackUpdateMessage on client.");
			return;
		}

		final ServerPlayerEntity playerEntity = ctx.getSender();
		if(playerEntity == null) {
			System.err.println("ServerPlayerEntity was null when ItemStackUpdateMessage was received.");
			return;
		}

		ctx.enqueueWork(() -> processMessage(message, playerEntity));
	}

	private static void processMessage(ItemStackUpdateMessage message, ServerPlayerEntity playerEntity) {
		playerEntity.inventory.setItem(message.getSlot(), message.getStack());
		playerEntity.inventory.setChanged();
	}

	public static boolean acceptsProtocol(String protocol) {
		return Furnish.MESSAGE_PROTOCOL_VERSION.equals(protocol);
	}
}
