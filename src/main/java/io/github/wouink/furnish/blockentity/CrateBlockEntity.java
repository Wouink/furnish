package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.container.ConditionalChestMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

public class CrateBlockEntity extends AbstractFurnitureBlockEntity {
    private static Predicate<ItemStack> FILTER = stack -> !stack.is(FurnishContents.CRATE_BLACKLIST_TAG);

    public CrateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.CRATE_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return ConditionalChestMenu.oneRow(syncId, playerInventory, this, FILTER);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return FILTER.test(itemStack);
    }
}
