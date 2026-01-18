package io.github.wouink.furnish.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This class represents furniture that only holds one item, e.g. Showcase, Plate or Shelf.
 *
 * Showcase, Plate and Shelf do not directly use this class because they need their specific BlockEntity type
 * for binding with their specific BlockEntityRenderer in the client code.
 */
public abstract class AbstractStackHoldingBlockEntity extends AbstractFurnitureBlockEntity {
    public AbstractStackHoldingBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    public ItemStack getHeldItem() {
        return getItem(0);
    }

    public ItemStack swap(ItemStack itemStack) {
        ItemStack result = getHeldItem();
        setItem(0, itemStack);
        return result;
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }

    @Override
    public boolean shouldUpdateRedstone() {
        return true;
    }
}
