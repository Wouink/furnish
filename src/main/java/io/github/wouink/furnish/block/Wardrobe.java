package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.PlacementHelper;
import io.github.wouink.furnish.blockentity.LargeFurnitureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class Wardrobe extends Cabinet {
    public static final BooleanProperty TOP = FurnishContents.Properties.TOP;
    public Wardrobe(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(TOP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TOP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockPos clicked = blockPlaceContext.getClickedPos();
        if(blockPlaceContext.getLevel().getBlockState(clicked.above()).canBeReplaced(blockPlaceContext)) {
            return defaultBlockState()
                    .setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                    .setValue(RIGHT, PlacementHelper.shouldPlaceRight(blockPlaceContext));
        } else {
            if(blockPlaceContext.getLevel().isClientSide()) {
                blockPlaceContext.getPlayer().displayClientMessage(Component.translatable("msg.furnish.furniture_too_big"), true);
            }
            return null;
        }
    }

    @Override
    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState previousState, boolean movedByPiston) {
        super.onPlace(blockState, level, blockPos, previousState, movedByPiston);
        if(!blockState.getValue(TOP).booleanValue()) {
            level.setBlock(blockPos.above(), blockState.setValue(TOP, true), Block.UPDATE_ALL);
        }
    }

    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if(blockState.getValue(TOP).booleanValue()) {
            BlockState below = levelAccessor.getBlockState(blockPos.below());
            if(below.is(this) && !below.getValue(TOP).booleanValue())
                levelAccessor.removeBlock(blockPos.below(), true);
        } else {
            BlockState above = levelAccessor.getBlockState(blockPos.above());
            if(above.is(this) && above.getValue(TOP).booleanValue())
                levelAccessor.removeBlock(blockPos.above(), true);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(TOP).booleanValue() ? null : new LargeFurnitureBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockState.getValue(TOP).booleanValue() ? blockPos.below() : blockPos);
        if(blockEntity instanceof LargeFurnitureBlockEntity furnitureBlockEntity)
            player.openMenu(furnitureBlockEntity);
        return InteractionResult.CONSUME;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }
}
