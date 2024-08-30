package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.block.util.FurnitureWithSound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

/*
    RandomizableContainerBlockEntity
    - can be renamed and placed in the world with this custom name
    - can be locked
    - handles loot table logic

    WorldlyContainer
    - defines the ability to put/take items from hoppers
 */

public abstract class FurnishInventoryBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private NonNullList<ItemStack> inventory;
    public FurnishInventoryBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        inventory = NonNullList.withSize(getCapacity(), ItemStack.EMPTY);
    }

    @Override
    protected Component getDefaultName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.inventory = nonNullList;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        super.setItem(i, itemStack);
        if(broadcastInventoryUpdates()) BlockEntityHelper.broadcastUpdate(this, broadcastInventoryUpdatesToRedstone());
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return getMenu(syncId, playerInventory);
    }

    @Override
    public int getContainerSize() {
        return getCapacity();
    }

    public abstract int getCapacity();
    public abstract AbstractContainerMenu getMenu(int syncId, Inventory playerInventory);

    public NonNullList<ItemStack> getItemsForRender() {
        return getItems();
    }

    public boolean broadcastInventoryUpdates() {
        return false;
    }

    public boolean broadcastInventoryUpdatesToRedstone() {
        return false;
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        if(!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, inventory, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if(!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, inventory, provider);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, getCapacity()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public void startOpen(Player player) {
        super.startOpen(player);
        if(!player.isSpectator()) {
            if(this.getBlockState().getBlock() instanceof FurnitureWithSound furnitureWithSound) {
                this.level.playSound(null, this.getBlockPos(), furnitureWithSound.getOpenSound(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }
    }

    @Override
    public void stopOpen(Player player) {
        super.stopOpen(player);
        if(this.getBlockState().getBlock() instanceof FurnitureWithSound furnitureWithSound) {
            this.level.playSound(null, this.getBlockPos(), furnitureWithSound.getCloseSound(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    // communication between client/server for rendering purposes

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithFullMetadata(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // force render update when a hopper removes an item

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack ret = super.removeItemNoUpdate(i);
        if(broadcastInventoryUpdates()) BlockEntityHelper.broadcastUpdate(this, true);
        return ret;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack ret = super.removeItem(i, j);
        if(broadcastInventoryUpdates()) BlockEntityHelper.broadcastUpdate(this, true);
        return ret;
    }
}
