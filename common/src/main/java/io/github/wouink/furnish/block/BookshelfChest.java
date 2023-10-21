package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.BookshelfChestBlockEntity;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BookshelfChest extends Block implements EntityBlock {
    public static final IntegerProperty CAPACITY = IntegerProperty.create("capacity", 0, 3);
    public BookshelfChest(Properties properties) {
        super(properties);
        FurnishBlocks.Bookshelf_Chests.add(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CAPACITY);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BookshelfChestBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        if(blockState.getBlock() != blockState2.getBlock() && level.getBlockEntity(blockPos) instanceof BookshelfChestBlockEntity bookshelf) {
            Containers.dropContents(level, blockPos, bookshelf);
            level.updateNeighbourForOutputSignal(blockPos, this);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide() && !player.isSpectator() && level.getBlockEntity(blockPos) instanceof BookshelfChestBlockEntity bookshelf) {
            bookshelf.unpackLootTable(player);
            player.openMenu(bookshelf);
        }
        return InteractionResult.CONSUME;
    }
}
