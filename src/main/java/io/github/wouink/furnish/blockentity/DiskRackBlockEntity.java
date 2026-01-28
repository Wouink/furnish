package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.container.DiskRackMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class DiskRackBlockEntity extends AbstractFurnitureBlockEntity {

    public DiskRackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.DISK_RACK_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return new DiskRackMenu(syncId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return DiskRackMenu.SIZE;
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }

    @Override
    public boolean shouldUpdateRedstone() {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return DiskRackMenu.FILTER.test(itemStack);
    }
}
