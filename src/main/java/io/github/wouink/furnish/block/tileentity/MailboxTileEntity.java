package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.container.MailboxContainer;
import io.github.wouink.furnish.block.util.TileEntityHelper;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MailboxTileEntity extends RandomizableContainerBlockEntity {
	public static final int SIZE = 18;
	private static final TagKey MAIL_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "mail"));
	protected NonNullList<ItemStack> inventory;
	private String owner;
	private String ownerDisplayName;

	public MailboxTileEntity(BlockPos pos, BlockState state) {
		super(FurnishData.TileEntities.TE_Mailbox.get(), pos, state);
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
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		ContainerHelper.saveAllItems(nbt, inventory);
		nbt.putString("Owner", owner == null ? "" : owner);
		nbt.putString("OwnerDisplayName", ownerDisplayName == null ? "" : ownerDisplayName);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(nbt, inventory);
		owner = nbt.getString("Owner");
		ownerDisplayName = nbt.getString("OwnerDisplayName");
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
				TileEntityHelper.broadcastUpdate(this, false);
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
		if(Furnish.CONFIG.restrictMailboxItems.get().booleanValue() && !stack.is(MAIL_TAG)) return stack;
		if(stack.getItem() instanceof Letter) Letter.signLetter(stack, "Anonymous Player");
		int slot = getFreeSlot();
		if(slot < getContainerSize()) {
			ItemStack result = inventory.set(slot, stack);
			TileEntityHelper.broadcastUpdate(this, false);

			if(result.isEmpty()) {
				Player mailboxOwner = level.getPlayerByUUID(getOwner());
				if (mailboxOwner != null) {
					if (hasCustomName()) {
						mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail_loc", getCustomName()), true);
					} else {
						mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail"), true);
					}
					if(mailboxOwner instanceof ServerPlayer serverPlayer)
					TileEntityHelper.playSoundToPlayer(serverPlayer, FurnishData.Sounds.Mail_Received.get(), SoundSource.MASTER, 1.0f, 1.0f);
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

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithFullMetadata();
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
}
