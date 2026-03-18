package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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

    @Override
    public void destroy(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        super.destroy(levelAccessor, blockPos, blockState);
        System.out.println("block destroyed");
    }

    // drop items upon destruction => handled automatically when block entity is a `Container` since 1.21.11

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
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    // get comparator redstone output
    @Override
    protected boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity != null)
            return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(blockEntity); // calculates if container, 0 otherwise
        return super.getAnalogOutputSignal(blockState, level, blockPos, direction);
    }
}
