package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.BookshelfChest;
import io.github.wouink.furnish.block.container.BookshelfChestMenu;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BookshelfChestBlockEntity extends FurnishInventoryBlockEntity {
    public static final int SIZE = 9;

    public BookshelfChestBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishRegistries.BookshelfChest_BlockEntity.get(), blockPos, blockState);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        super.setItem(i, itemStack);
        updateCapacity();
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        ItemStack ret = super.removeItem(i, j);
        updateCapacity();
        return ret;
    }

    @Override
    public int getCapacity() {
        return SIZE;
    }

    @Override
    public AbstractContainerMenu getMenu(int syncId, Inventory playerInventory) {
        return new BookshelfChestMenu(syncId, playerInventory, this);
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return BookshelfChestMenu.canPlace(itemStack);
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
