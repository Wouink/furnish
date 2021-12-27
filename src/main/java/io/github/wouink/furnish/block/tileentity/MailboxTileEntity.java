package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.container.MailboxContainer;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.UUID;

public class MailboxTileEntity extends LockableLootTileEntity {
	public static final int SIZE = 18;
	private static final ResourceLocation MAIL_TAG = new ResourceLocation(Furnish.MODID, "mail");
	protected NonNullList<ItemStack> inventory;
	private String owner;
	private String ownerDisplayName;

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
		nbt.putString("Owner", owner == null ? "" : owner);
		nbt.putString("OwnerDisplayName", ownerDisplayName);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
		owner = nbt.getString("Owner");
		ownerDisplayName = nbt.getString("OwnerDisplayName");
	}

	@Override
	protected Container createMenu(int syncId, PlayerInventory playerInventory) {
		return new MailboxContainer(syncId, playerInventory, this);
	}

	@Override
	public int getContainerSize() {
		return SIZE;
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return false;
	}

	public void updateDisplayName(PlayerEntity playerEntity) {
		if(isOwner(playerEntity)) {
			String playerName = playerEntity.getGameProfile().getName();
			if(!ownerDisplayName.equals(playerName)) {
				ownerDisplayName = playerName;
				level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
			}
		}
	}

	public void setOwner(PlayerEntity playerEntity) {
		owner = playerEntity.getStringUUID();
		updateDisplayName(playerEntity);
	}

	public UUID getOwner() {
		return UUID.fromString(owner);
	}

	public boolean hasOwner() {
		return owner != null && !owner.isEmpty();
	}

	public boolean isOwner(PlayerEntity playerEntity) {
		return hasOwner() && playerEntity.getStringUUID().equals(owner);
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
		if(Furnish.Furnish_Config.restrictMailboxItems.get().booleanValue() && !stack.getItem().getTags().contains(MAIL_TAG)) return stack;
		if(stack.getItem() instanceof Letter) Letter.signLetter(stack, "Anonymous Player");
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
		return (!hasOwner() || ownerDisplayName.isEmpty()) ? null : new StringTextComponent(ownerDisplayName);
	}

	private int getFreeSlot() {
		int slot = 0;
		while(slot < inventory.size() && !inventory.get(slot).isEmpty()) slot++;
		return slot;
	}


	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, 1234, save(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState state = this.getBlockState();
		load(state, pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		load(state, tag);
	}
}
