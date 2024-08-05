package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.container.MailboxContainer;
import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class MailboxBlockEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 18;
	protected NonNullList<ItemStack> inventory;
	private String owner;
	private String ownerDisplayName;

	public MailboxBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Mailbox_BlockEntity.get(), pos, state);
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
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.saveAdditional(compoundTag, provider);
		ContainerHelper.saveAllItems(compoundTag, inventory, provider);
		compoundTag.putString("Owner", owner == null ? "" : owner);
		compoundTag.putString("OwnerDisplayName", ownerDisplayName == null ? "" : ownerDisplayName);
	}

	@Override
	protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.loadAdditional(compoundTag, provider);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compoundTag, inventory, provider);
		owner = compoundTag.getString("Owner");
		ownerDisplayName = compoundTag.getString("OwnerDisplayName");
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
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

	public void updateDisplayName(Player playerEntity) {
		if(isOwner(playerEntity)) {
			String playerName = playerEntity.getGameProfile().getName();
			if(ownerDisplayName == null || !ownerDisplayName.equals(playerName)) {
				ownerDisplayName = playerName;
				BlockEntityHelper.broadcastUpdate(this, false);
			}
		}
	}

	public void setOwner(Player playerEntity) {
		owner = playerEntity.getStringUUID();
		updateDisplayName(playerEntity);
	}

	public UUID getOwner() {
		return UUID.fromString(owner);
	}

	public boolean hasOwner() {
		return owner != null && !owner.isEmpty();
	}

	public boolean isOwner(Player playerEntity) {
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
		if(!this.getBlockState().is(FurnishRegistries.BYPASSES_MAIL_TAG_TAG) && !stack.is(FurnishRegistries.MAIL_TAG)) return stack;
		if(stack.getItem() instanceof Letter) Letter.signLetter(stack, "Anonymous Player");
		int slot = getFreeSlot();
		if(slot < getContainerSize()) {
			ItemStack result = inventory.set(slot, stack);
			BlockEntityHelper.broadcastUpdate(this, false);

			if(result.isEmpty()) {
				Player mailboxOwner = level.getPlayerByUUID(getOwner());
				if (mailboxOwner != null) {
					if (hasCustomName()) {
						mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail_loc", getCustomName()), true);
					} else {
						mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail"), true);
					}
					if(mailboxOwner instanceof ServerPlayer serverPlayer)
					BlockEntityHelper.playSoundToPlayer(serverPlayer, FurnishRegistries.Mail_Received_Sound.get(), SoundSource.MASTER, 1.0f, 1.0f);
				}
			}

			return result;
		}
		return stack;
	}

	public Component getOwnerDisplayName() {
		return (!hasOwner() || ownerDisplayName.isEmpty()) ? null : Component.literal(ownerDisplayName);
	}

	private int getFreeSlot() {
		int slot = 0;
		while(slot < inventory.size() && !inventory.get(slot).isEmpty()) slot++;
		return slot;
	}

	// communication between client/server for rendering purposes

	/*
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}
	 */

	/*
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
	 */

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveWithFullMetadata(provider);
	}
}
