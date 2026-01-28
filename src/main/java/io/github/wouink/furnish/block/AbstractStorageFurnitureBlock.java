package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.InteractionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * This code is common to every Furniture that stores content in Furnish.
 * It handles placing the block with the right orientation, opening the menu and dropping the items upon destruction.
 */
public abstract class AbstractStorageFurnitureBlock extends HorizontalDirectionalBlock implements EntityBlock {
    protected AbstractStorageFurnitureBlock(Properties properties) {
        super(properties);
    }

    // add the FACING property
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    // place with the right orientation
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    // drop items upon destruction
    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean movedByPiston) {
        if(!level.isClientSide() && blockState.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity != null && blockEntity instanceof Container container)
                Containers.dropContents(level, blockPos, container);
        }
        super.onRemove(blockState, level, blockPos, newState, movedByPiston);
    }

    // open menu on right click
    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity != null && blockEntity instanceof MenuProvider menuProvider)
            player.openMenu(menuProvider);
        return InteractionResult.CONSUME;
    }

    // open menu on right click
    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    // get comparator redstone output
    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity != null)
            return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(blockEntity); // calculates if container, 0 otherwise
        return super.getAnalogOutputSignal(blockState, level, blockPos);
    }
}
