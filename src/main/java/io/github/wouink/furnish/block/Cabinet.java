package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.PlacementHelper;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class Cabinet extends Drawer {
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
}
