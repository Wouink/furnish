package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.Mailbox;
import io.github.wouink.furnish.container.ConditionalChestMenu;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;
import java.util.function.Predicate;

public class MailboxBlockEntity extends AbstractFurnitureBlockEntity {
    private static Predicate<ItemStack> FILTER = stack -> false;

    private UUID owner;
    private String ownerDisplayName;

    public MailboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.MAILBOX_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return ConditionalChestMenu.twoRows(syncId, inventory, this, FILTER);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return FILTER.test(itemStack);
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean shouldUpdateRedstone() {
        return true;
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if(compoundTag.contains("Owner")) owner = compoundTag.getUUID("Owner");
        if(compoundTag.contains("OwnerDisplayName")) ownerDisplayName = compoundTag.getString("OwnerDisplayName");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if(owner != null) compoundTag.putUUID("Owner", owner);
        if(ownerDisplayName != null) compoundTag.putString("OwnerDisplayName", ownerDisplayName);
    }

    // ownership handling

    public UUID getOwner() {
        return owner;
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public boolean isOwner(Entity entity) {
        return hasOwner() && entity.getUUID().equals(getOwner());
    }

    public void setOwner(Entity entity) {
        owner = entity.getUUID();
        if(entity.hasCustomName()) ownerDisplayName = entity.getCustomName().getString();
        else if(entity instanceof ServerPlayer player)
            ownerDisplayName = player.getGameProfile().getName();
        else ownerDisplayName = null;
        broadcastChanges();
    }

    public Component getOwnerDisplayName() {
        return (!hasOwner() || ownerDisplayName == null) ? null : Component.literal(ownerDisplayName);
    }

    // mail handling

    public boolean hasMail() {
        for(int i = 0; i < getContainerSize(); i++)
            if(!getItem(i).isEmpty()) return true;
        return false;
    }

    public boolean isFull() {
        return !getItems().contains(ItemStack.EMPTY);
    }

    private int getNextFreeSlot() {
        int slot = 0;
        while(slot < getContainerSize() && !getItem(slot).isEmpty()) slot++;
        return slot;
    }

    private void updateMyMailboxFlag() {
        boolean flagRaised = getBlockState().getValue(Mailbox.HAS_MAIL);
        if(flagRaised == hasMail()) return;
        level.setBlock(getBlockPos(), getBlockState().setValue(Mailbox.HAS_MAIL, hasMail()), Block.UPDATE_ALL);
        level.playSound(null, getBlockPos(), FurnishContents.MAILBOX_FLAG_TOGGLE, SoundSource.BLOCKS);
    }

    private static void playSoundToClient(ServerPlayer player, SoundEvent sound, SoundSource source, float volume, float pitch) {
        ClientboundSoundPacket pkt = new ClientboundSoundPacket((Holder<SoundEvent>) sound, source,
                player.getX(), player.getY(), player.getZ(), volume, pitch, player.level().getRandom().nextLong());
        player.connection.send(pkt);
    }

    private void notifyOwner() {
        Player target = level.getPlayerByUUID(getOwner());
        if(target != null) {
            String message = "msg.furnish.mailbox.new_mail";
            if(hasCustomName()) message += "_loc";
            target.displayClientMessage(Component.translatable(message), true);
            if(target instanceof ServerPlayer serverPlayer)
                playSoundToClient(serverPlayer, FurnishContents.NEW_MAIL, SoundSource.MASTER, 1.0f, 1.0f);
        }
    }

    public ItemStack insertMail(ItemStack itemStack) {
        ItemStack ret = itemStack;
        if(getBlockState().is(FurnishContents.BYPASSES_MAIL) || itemStack.is(FurnishContents.MAIL)) {
            if(itemStack.getItem() instanceof Letter) Letter.sign(itemStack, Letter.ANON_PLAYER);
            int slot = getNextFreeSlot();
            if(slot < getContainerSize()) {
                setItem(slot, itemStack); // broadcasts changes
                updateMyMailboxFlag();
                notifyOwner();
                ret = ItemStack.EMPTY;
            }
        }
        return ret;
    }

    @Override
    public void stopOpen(Player containerUser) {
        super.stopOpen(containerUser);
        updateMyMailboxFlag();
    }
}
