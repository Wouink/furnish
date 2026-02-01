package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

public class ShowcaseBlockEntity extends AbstractStackHoldingBlockEntity {
    public ShowcaseBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.SHOWCASE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }
}
