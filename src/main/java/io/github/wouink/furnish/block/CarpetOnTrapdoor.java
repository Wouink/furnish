package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.InteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetOnTrapdoor extends HorizontalDirectionalBlock {
    private static final VoxelShape CLOSED_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private Block clone;

    public CarpetOnTrapdoor(Properties properties) {
        super(properties);
    }

    public void setClone(Block clone) {
        this.clone = clone;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        if(clone != null) return new ItemStack(clone);
        return ItemStack.EMPTY;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(CarpetOnTrapdoor::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, OPEN);
    }

    @Override
    protected BlockState updateShape(BlockState blockState, Direction direction, BlockState fromState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos fromPos) {
        BlockState below = levelAccessor.getBlockState(blockPos.below());
        if(below.getBlock() instanceof TrapDoorBlock) {
            if(below.getValue(TrapDoorBlock.HALF) == Half.TOP)
                return blockState.setValue(OPEN, below.getValue(TrapDoorBlock.OPEN));
        } else if(levelAccessor.isEmptyBlock(blockPos.below()))
            return Blocks.AIR.defaultBlockState();
        return blockState;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(OPEN) ? Shapes.empty() : CLOSED_SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        BlockState below = level.getBlockState(blockPos.below());
        if(below.getBlock() instanceof TrapDoorBlock) {
            return below.useWithoutItem(level, player, blockHitResult.withPosition(blockPos.below()));
        }
        return InteractionResult.FAIL;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    public static boolean attemptPlacement(LevelAccessor level, BlockPos trapdoorPos, WoolCarpetBlock carpet) {
        // ensure the feature is enabled
        if(!carpet.defaultBlockState().is(FurnishContents.PLACE_ON_TRAPDOOR)) return false;

        // ensure we're using a vanilla carpet
        if(!BuiltInRegistries.BLOCK.getKey(carpet).getNamespace().equals("minecraft")) return false;

        // ensure we can place the carpet on the trapdoor
        if(!level.isEmptyBlock(trapdoorPos.above())) return false;
        BlockState target = level.getBlockState(trapdoorPos);
        if(!(target.getBlock() instanceof TrapDoorBlock)) return false;
        if(target.getValue(TrapDoorBlock.HALF) != Half.TOP) return false;

        // place the carpet
        DyeColor color = carpet.getColor();
        BlockState toPlace = FurnishContents.COLORED_SETS.get(color).carpetOnTrapdoor.defaultBlockState();
        toPlace = toPlace.setValue(BlockStateProperties.HORIZONTAL_FACING, target.getValue(BlockStateProperties.HORIZONTAL_FACING));
        toPlace = toPlace.setValue(BlockStateProperties.OPEN, target.getValue(BlockStateProperties.OPEN));
        level.setBlock(trapdoorPos.above(), toPlace, UPDATE_ALL);
        level.playSound(null, trapdoorPos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS);

        return true;
    }
}
