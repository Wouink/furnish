package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.blockentity.ShelfBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Shelf extends AbstractItemDisplayBlock {
    private static final VoxelShape[] SHAPES = ShapeHelper.getRotatedShapes(Block.box(0, 2, 0, 5, 4, 16));

    public Shelf(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Shelf::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShelfBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES[blockState.getValue(FACING).ordinal() - 2];
    }
}
