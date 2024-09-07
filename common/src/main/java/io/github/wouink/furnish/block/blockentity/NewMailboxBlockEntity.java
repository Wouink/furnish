package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.NewMailbox;
import io.github.wouink.furnish.block.container.MailboxMenu;
import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class NewMailboxBlockEntity extends FurnishInventoryBlockEntity {
    public static final int SIZE = 18;
    private UUID owner;
    private String ownerDisplayName;
    public NewMailboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishRegistries.Mailbox_BlockEntity.get(), blockPos, blockState);
    }

    @Override
    public int getCapacity() {
        return SIZE;
    }

    @Override
    public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
        return new MailboxMenu(syncId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        if(tag.contains("Owner")) owner = tag.getUUID("Owner");
        if(tag.contains("OwnerDisplayName")) ownerDisplayName = tag.getString("OwnerDisplayName");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if(owner != null) tag.putUUID("Owner", owner);
        if(ownerDisplayName != null) tag.putString("OwnerDisplayName", ownerDisplayName);
    }

    @Override
    public boolean broadcastInventoryUpdates() {
        return true;
    }

    @Override
    public boolean broadcastInventoryUpdatesToRedstone() {
        return true;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwner(Entity entity) {
        return hasOwner() && entity.getUUID().equals(getOwner());
    }

    public void setOwner(Entity entity) {
        owner = entity.getUUID();
        if(entity instanceof ServerPlayer player) {
            ownerDisplayName = player.getGameProfile().getName();
        } else if(entity.hasCustomName()) ownerDisplayName = entity.getCustomName().getString();
        else ownerDisplayName = null;
        BlockEntityHelper.broadcastUpdate(this, false);
    }

    public Component getOwnerDisplayName() {
        return (!hasOwner() || ownerDisplayName == null) ? null : Component.literal(ownerDisplayName);
    }

    public boolean hasMail() {
        for(int i = 0; i < SIZE; i++) {
            if(!getItem(i).isEmpty()) return true;
        }
        return false;
    }

    public boolean isFull() {
        return !getItems().contains(ItemStack.EMPTY);
    }

    public int nextFreeSlot() {
        int slot = 0;
        while(slot < getCapacity() && !getItem(slot).isEmpty()) slot++;
        return slot;
    }

    public ItemStack insertMail(ItemStack mail) {
        if(getBlockState().is(FurnishRegistries.BYPASSES_MAIL_TAG_TAG) || mail.is(FurnishRegistries.MAIL_TAG)) {
            if(mail.getItem() instanceof Letter) Letter.signLetter(mail, "Anonymous Player");
            int slot = nextFreeSlot();
            if(slot < getCapacity()) {
                setItem(slot, mail);
                mail = ItemStack.EMPTY;
                updateBlock();
                notifyOwner();
            }
        }
        return mail;
    }

    private void notifyOwner() {
        Player mailboxOwner = level.getPlayerByUUID(getOwner());
        if(mailboxOwner != null) {
            if(hasCustomName()) {
                mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail_loc", getCustomName()), true);
            } else {
                mailboxOwner.displayClientMessage(Component.translatable("msg.furnish.mailbox.new_mail"), true);
            }
            if(mailboxOwner instanceof ServerPlayer serverPlayer) {
                BlockEntityHelper.playSoundToPlayer(serverPlayer, FurnishRegistries.Mail_Received_Sound.get(), SoundSource.MASTER, 1.0f, 1.0f);
            }
        }
    }

    private void updateBlock() {
        level.setBlock(getBlockPos(), getBlockState().setValue(NewMailbox.HAS_MAIL, hasMail()), Block.UPDATE_ALL);
        level.playSound(null, getBlockPos(), FurnishRegistries.Mailbox_Update_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    @Override
    public void stopOpen(Player player) {
        super.stopOpen(player);
        updateBlock();
    }
}
