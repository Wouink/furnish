package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.state.BlockState;

public class SmallFurnitureBlockEntity extends AbstractFurnitureBlockEntity {
    public SmallFurnitureBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.SMALL_FURNITURE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory inventory) {
        return ChestMenu.threeRows(syncId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }
}
