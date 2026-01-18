package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.blockentity.AbstractStackHoldingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * This code is common to all the blocks which purpose is to display an item, e.g. Plate, Showcase or Shelf.
 */
public abstract class AbstractItemDisplayBlock extends HorizontalDirectionalBlock implements EntityBlock {
    protected AbstractItemDisplayBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    // swap the item on right click with an item
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return ItemInteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity != null && blockEntity instanceof AbstractStackHoldingBlockEntity stackHoldingBlockEntity) {
            // TODO play a sound?
            player.setItemInHand(interactionHand, stackHoldingBlockEntity.swap(itemStack));
        }
        return ItemInteractionResult.CONSUME;
    }

    // remove the item (=swap with empty itemstack) on right click with empty hand
    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        return InteractionHelper.toResult(useItemOn(ItemStack.EMPTY, blockState, level, blockPos, player, InteractionHand.MAIN_HAND, blockHitResult));
    }

    // if there is an item inside, return this item on mouse middle click
    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = levelReader.getBlockEntity(blockPos);
        if(blockEntity != null && blockEntity instanceof AbstractStackHoldingBlockEntity stackHoldingBlockEntity) {
            ItemStack stack = stackHoldingBlockEntity.getHeldItem();
            if(!stack.isEmpty()) {
                stack.setCount(1);
                return stack;
            }
        }
        return super.getCloneItemStack(levelReader, blockPos, blockState);
    }

    // drop the item when broken
    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean pistonMoved) {
        if(!blockState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof AbstractStackHoldingBlockEntity stackHoldingBlockEntity) {
                ItemStack stack = stackHoldingBlockEntity.getHeldItem();
                if(!stack.isEmpty())
                    level.addFreshEntity(new ItemEntity(
                            level,
                            blockPos.getX() + .5,
                            blockPos.getY() + .5,
                            blockPos.getZ() + .5,
                            stack
                    ));
            }
        }
        super.onRemove(blockState, level, blockPos, newState, pistonMoved);
    }
}
