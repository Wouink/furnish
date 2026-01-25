package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.blockentity.SmallFurnitureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class Drawer extends AbstractStorageFurnitureBlock implements SoundProvider {
    public Drawer(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Drawer::new);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SmallFurnitureBlockEntity(blockPos, blockState);
    }

    @Override
    public SoundEvent getOpenSound() {
        return FurnishContents.DRAWER_OPEN;
    }

    @Override
    public SoundEvent getCloseSound() {
        return FurnishContents.DRAWER_CLOSE;
    }
}
