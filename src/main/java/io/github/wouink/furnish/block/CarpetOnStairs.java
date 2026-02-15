package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.ShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
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
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
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
    protected BlockState updateShape(BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos fromPos, BlockState fromState, RandomSource randomSource) {
        return blockState.canSurvive(levelReader, blockPos) ? super.updateShape(blockState, levelReader, scheduledTickAccess, blockPos, direction, fromPos, fromState, randomSource) : Blocks.AIR.defaultBlockState();
    }

    public static boolean attemptPlacement(LevelAccessor level, BlockPos stairsPos, WoolCarpetBlock carpet) {
        // ensure the feature is enabled
        if(!carpet.defaultBlockState().is(FurnishContents.PLACE_ON_STAIRS)) return false;

        // ensure we're using a vanilla carpet
        if(!BuiltInRegistries.BLOCK.getKey(carpet).getNamespace().equals("minecraft")) return false;

        // ensure we can place the carpet on the stairs
        if(!level.isEmptyBlock(stairsPos.above())) return false;
        BlockState target = level.getBlockState(stairsPos);
        if(!(target.getBlock() instanceof StairBlock)) return false;
        if(target.getValue(StairBlock.SHAPE) != StairsShape.STRAIGHT) return false;
        if(target.getValue(StairBlock.HALF) != Half.BOTTOM) return false;

        // place the carpet
        DyeColor color = carpet.getColor();
        BlockState toPlace = FurnishContents.COLORED_SETS.get(color).carpetOnStairs.defaultBlockState();
        toPlace = toPlace.setValue(BlockStateProperties.HORIZONTAL_FACING, target.getValue(BlockStateProperties.HORIZONTAL_FACING));
        level.setBlock(stairsPos.above(), toPlace, UPDATE_ALL);
        level.playSound(null, stairsPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS);

        return true;
    }
}
