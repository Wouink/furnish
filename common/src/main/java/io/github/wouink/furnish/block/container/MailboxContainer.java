package io.github.wouink.furnish.block.container;

import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;

public class MailboxContainer extends ConditionalSlotContainer {
	public MailboxContainer(int syncId, Inventory playerInventory, Container inventory) {
		super(2, ConditionalSlot::never, FurnishRegistries.Mailbox_Container.get(), syncId, playerInventory, inventory);
	}

	public MailboxContainer(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(MailboxTileEntity.SIZE));
	}
}
