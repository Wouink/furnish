package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.block.SoundProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.IntStream;

/**
    This class is the base of every Furnish furniture.
    It handles common logic (loading/saving, playing open/close sound when needed, synchronizing clients,
    updating redstone etc.).
    It can be easily extended with new features (ex: filtering items that can be inserted using hoppers
    by overriding `canPlaceItemThroughFace`).
    It is possible to set up item filtering in the menu too using the conditional containers provided
    in the `container` package.

    ---

    This class is a subclass of the following classes:

    RandomizableContainerBlockEntity
        - can be renamed and placed in the world, keeping the custom name
        - can be locked
        - handles loot table logic

    WorldlyContainer
        - defines the ability to put/take items with hoppers
*/

public abstract class AbstractFurnitureBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private NonNullList<ItemStack> inventory;
    private int users = 0;

    public AbstractFurnitureBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }

    /**
     * Should the furniture notify clients when it's inventory changes? (for visual updates)
     * @return true if clients should be updated
     */
    public boolean shouldUpdateClient() {
        return false;
    }

    /**
     * Should the redstone output of this block be updated when it's inventory changes?
     * @return true if redstone should be updated
     */
    public boolean shouldUpdateRedstone() {
        return true;
    }

    /**
     * Which sound is played when the inventory is opened?
     * @return the open sound
     */
    public SoundEvent getOpenSound() {
        if(getBlockState().getBlock() instanceof SoundProvider provider) return provider.getOpenSound();
        return null;
    }

    /**
     * Which sound is played when the inventory is closed?
     * @return the sound event
     */
    public SoundEvent getCloseSound() {
        if(getBlockState().getBlock() instanceof SoundProvider provider) return provider.getCloseSound();
        return null;
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
    protected void setItems(NonNullList<ItemStack> newInventory) {
        inventory = newInventory;
        broadcastChanges();
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    // save/load


    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if(!tryLoadLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, inventory, provider);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if(!tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, inventory, provider);
        }
    }

    // client/server sync for visual updates + redstone

    protected void broadcastChanges() {
        setChanged();
        if(shouldUpdateClient())
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        if(shouldUpdateRedstone())
            level.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        super.setItem(i, itemStack);
        broadcastChanges();
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack ret = super.removeItem(i, j);
        broadcastChanges();
        return ret;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack ret = super.removeItemNoUpdate(i);
        broadcastChanges();
        return ret;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithFullMetadata(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    // open/close sound

    @Override
    public void startOpen(Player containerUser) {
        super.startOpen(containerUser);
        if(users == 0 && getOpenSound() != null)
            level.playSound(null, getBlockPos(), getOpenSound(), SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
        users++;
    }

    @Override
    public void stopOpen(Player containerUser) {
        super.stopOpen(containerUser);
        if(users == 1 && getCloseSound() != null)
            level.playSound(null, getBlockPos(), getCloseSound(), SoundSource.PLAYERS, 0.5f, level.random.nextFloat() * 0.1f + 0.9f);
        users--;
    }
}
