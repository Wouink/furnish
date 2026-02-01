package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.block.util.PlacementHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;

public class Cabinet extends Drawer implements SoundProvider {
    public static final BooleanProperty RIGHT = FurnishContents.Properties.RIGHT;
    public Cabinet(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(RIGHT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return super.getStateForPlacement(blockPlaceContext)
                .setValue(RIGHT, PlacementHelper.shouldPlaceRight(blockPlaceContext));
    }

    @Override
    public SoundEvent getOpenSound() {
        if(this == FurnishContents.LOCKER || this == FurnishContents.SMALL_LOCKER) return FurnishContents.LOCKER_OPEN;
        WoodenSet spruceSet = FurnishContents.WOODEN_SETS.get(WoodType.SPRUCE);
        if(this == spruceSet.cabinet || this == spruceSet.wardrobe) return FurnishContents.SPRUCE_CABINET_OPEN;
        return FurnishContents.CABINET_OPEN;
    }

    @Override
    public SoundEvent getCloseSound() {
        if(this == FurnishContents.LOCKER || this == FurnishContents.SMALL_LOCKER) return FurnishContents.LOCKER_CLOSE;
        WoodenSet spruceSet = FurnishContents.WOODEN_SETS.get(WoodType.SPRUCE);
        if(this == spruceSet.cabinet || this == spruceSet.wardrobe) return FurnishContents.SPRUCE_CABINET_CLOSE;
        return FurnishContents.CABINET_CLOSE;
    }
}
