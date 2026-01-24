package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.ShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetOnStairs extends HorizontalDirectionalBlock {
    public static final VoxelShape[] CARPET_SHAPE = ShapeHelper.getMergedShapes(
            ShapeHelper.getRotatedShapes(Block.box(8, 0, 0, 16, 1, 16)),
            ShapeHelper.getRotatedShapes(Block.box(7, -8, 0, 8, 1, 16)),
            ShapeHelper.getRotatedShapes(Block.box(0, -8, 0, 7, -7, 16))
    );
    private Block clone = null;

    public CarpetOnStairs(Properties properties) {
        super(properties);
    }

    public void setClone(Block clone) {
        this.clone = clone;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if(clone != null) return new ItemStack(clone);
        else return ItemStack.EMPTY;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(CarpetOnStairs::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return CARPET_SHAPE[blockState.getValue(FACING).ordinal() - 2];
    }

    @Override
    protected boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return (levelReader.getBlockState(blockPos.below()).getBlock() instanceof StairBlock);
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState fromState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos fromPos) {
        return blockState.canSurvive(levelAccessor, blockPos) ? super.updateShape(blockState, direction, fromState, levelAccessor, blockPos, fromPos) : Blocks.AIR.defaultBlockState();
    }
}
