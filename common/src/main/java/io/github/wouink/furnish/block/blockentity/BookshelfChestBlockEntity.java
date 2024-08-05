package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.BookshelfChest;
import io.github.wouink.furnish.block.container.BookshelfChestMenu;
import io.github.wouink.furnish.block.util.BlockEntityHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BookshelfChestBlockEntity extends RandomizableContainerBlockEntity {
    protected NonNullList<ItemStack> inventory;
    public static final int SIZE = 9;

    public BookshelfChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishRegistries.BookshelfChest_BlockEntity.get(), blockPos, blockState);
        inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        inventory = nonNullList;
        updateCapacity();
        BlockEntityHelper.broadcastUpdate(this, true);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        this.inventory = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        if(!tryLoadLootTable(compoundTag)) ContainerHelper.loadAllItems(compoundTag, inventory, provider);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if(!trySaveLootTable(compoundTag)) ContainerHelper.saveAllItems(compoundTag, inventory, provider);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        super.setItem(i, itemStack);
        updateCapacity();
        BlockEntityHelper.broadcastUpdate(this, true);
    }

    @Override
    protected Component getDefaultName() {
        return this.getBlockState().getBlock().getName();
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new BookshelfChestMenu(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithFullMetadata(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public int getFilledSlots() {
        int filledslots = 0;

        for (int i = 0; i < SIZE; i++) {
            if (!getItem(i).isEmpty()) {
                filledslots++;
            }
        }

        return filledslots;
    }

    public void updateCapacity() {
        if (getLevel() == null) return;

        var filledslots = getFilledSlots();
        int capacity;

        var state = getLevel().getBlockState(getBlockPos());

        if (state.getBlock() instanceof BookshelfChest) {
            if (filledslots == 0) {
                capacity = 0;
            } else if (filledslots >= 1 && filledslots <= Math.ceil(SIZE * 0.44)) {
                capacity = 1;
            } else if (filledslots < SIZE) {
                capacity = 2;
            } else {
                capacity = 3;
            }

            state = state.setValue(BookshelfChest.CAPACITY, capacity);
            getLevel().setBlockAndUpdate(getBlockPos(), state);
        }
    }
}
