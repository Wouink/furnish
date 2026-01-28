package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.RecycleBin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RecycleBinBlockEntity extends AbstractFurnitureBlockEntity {
    public RecycleBinBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.RECYCLE_BIN_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new DispenserMenu(syncId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }

    @Override
    public boolean shouldUpdateRedstone() {
        return true;
    }

    public boolean empty() {
        SoundEvent sound = ((RecycleBin)getBlockState().getBlock()).getSound();
        if(sound != null) {
            for(int slot = 0; slot < getContainerSize(); slot++) {
                if(!getItem(slot).isEmpty()) {
                    level.playSound(null, getBlockPos(), sound, SoundSource.BLOCKS);
                    setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY)); // broadcasts changes
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack addItem(ItemStack itemStack) {
        ItemStack ret = itemStack;
        for(int slot = 0; slot < getContainerSize(); slot++) {
            if(getItem(slot).isEmpty()) {
                setItem(slot, itemStack); // broadcasts changes
                ret = ItemStack.EMPTY;
                break;
            }
        }
        return ret;
    }
}
