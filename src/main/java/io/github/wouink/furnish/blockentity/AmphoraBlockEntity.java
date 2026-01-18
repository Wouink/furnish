package io.github.wouink.furnish.blockentity;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.level.block.state.BlockState;

public class AmphoraBlockEntity extends AbstractFurnitureBlockEntity {
    public AmphoraBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(FurnishContents.AMPHORA_BLOCK_ENTITY, blockPos, blockState);
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
    public SoundEvent getOpenSound() {
        return FurnishContents.AMPHORA_OPEN;
    }

    @Override
    public SoundEvent getCloseSound() {
        return FurnishContents.AMPHORA_CLOSE;
    }
}
