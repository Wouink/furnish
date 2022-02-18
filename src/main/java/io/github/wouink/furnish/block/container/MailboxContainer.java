package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class MailboxContainer extends ConditionalSlotContainer {
	public MailboxContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(2, ConditionalSlot::never, FurnishData.Containers.Mailbox.get(), syncId, playerInventory, inventory);
	}

	public MailboxContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(MailboxTileEntity.SIZE));
	}
}
