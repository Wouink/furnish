package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class MailboxTileEntity extends LockableLootTileEntity {
	private static final ResourceLocation MAIL_TAG = new ResourceLocation(Furnish.MODID, "mail");
	protected NonNullList<ItemStack> inventory;
	private String owner;

	protected MailboxTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public MailboxTileEntity() {
		super(FurnishManager.TileEntities.Mailbox.get());
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> newInventory) {
		inventory = newInventory;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		ItemStackHelper.saveAllItems(nbt, inventory);
		nbt.putString("Owner", owner);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
		owner = nbt.getString("Owner");
	}

	@Override
	protected Container createMenu(int windowId, PlayerInventory playerInventory) {
		return new ChestContainer(ContainerType.GENERIC_9x2, windowId, playerInventory, this, 2);
	}

	@Override
	public int getContainerSize() {
		return 18;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return false;
	}

	public void setOwner(PlayerEntity playerEntity) {
		owner = playerEntity.getStringUUID();
	}

	public UUID getOwner() {
		return UUID.fromString(owner);
	}

	public boolean hasOwner() {
		return owner != null && !owner.isEmpty();
	}

	public boolean isOwner(PlayerEntity playerEntity) {
		return playerEntity.getStringUUID().equals(owner);
	}

	public boolean hasMail() {
		for(int i = 0; i < inventory.size(); i++) {
			if(!inventory.get(i).isEmpty()) return true;
		}
		return false;
	}

	public boolean isFull() {
		return !inventory.contains(ItemStack.EMPTY);
	}

	public ItemStack addMail(ItemStack stack) {
		if(!stack.getItem().getTags().contains(MAIL_TAG)) return stack;
		int slot = getFreeSlot();
		if(slot < getContainerSize()) {
			ItemStack result = inventory.set(slot, stack);

			if(result.isEmpty()) {
				PlayerEntity mailboxOwner = level.getPlayerByUUID(getOwner());
				if (mailboxOwner != null) {
					if (hasCustomName()) {
						mailboxOwner.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.new_mail_loc", getCustomName()), true);
					} else {
						mailboxOwner.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.new_mail"), true);
					}
				}
			}

			return result;
		}
		return stack;
	}

	public ITextComponent getOwnerDisplayName() {
		if(hasOwner()) {
			PlayerEntity mailboxOwner = level.getPlayerByUUID(getOwner());
			if (mailboxOwner != null) {
				return mailboxOwner.getDisplayName();
			}
		}
		return null;
	}

	private int getFreeSlot() {
		int slot = 0;
		while(slot < inventory.size() && !inventory.get(slot).isEmpty()) slot++;
		return slot;
	}
}
