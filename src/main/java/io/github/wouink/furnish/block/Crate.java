package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.TooltipHelper;
import io.github.wouink.furnish.blockentity.AbstractFurnitureBlockEntity;
import io.github.wouink.furnish.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// TODO test that it drops as an item containing its contents
// TODO test that the tag filtering works

public class Crate extends Block implements EntityBlock {
    public Crate(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrateBlockEntity(blockPos, blockState);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        TooltipHelper.appendInventoryContents(itemStack, list);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof AbstractFurnitureBlockEntity storageBlockEntity) {
            player.openMenu(storageBlockEntity);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
    }

    // copies the content on mouse wheel click
    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        ItemStack result = super.getCloneItemStack(levelReader, blockPos, blockState);
        levelReader.getBlockEntity(blockPos, FurnishContents.CRATE_BLOCK_ENTITY).ifPresent(
                crateBlockEntity -> {
                    crateBlockEntity.saveToItem(result, levelReader.registryAccess());
                }
        );
        return result;
    }

    // drop as an item containing the crate's content - copied from shulker box block
    @Override
    public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity != null && blockEntity instanceof CrateBlockEntity crateBlockEntity) {
            if (!level.isClientSide && player.isCreative() && !crateBlockEntity.isEmpty()) {
                ItemStack itemStack = new ItemStack(this);
                itemStack.applyComponents(blockEntity.collectComponents());
                ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            } else {
                crateBlockEntity.unpackLootTable(player);
            }
        }

        return super.playerWillDestroy(level, blockPos, blockState, player);
    }

    // copied from shulker box block
    @Override
    protected List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
        BlockEntity blockEntity = (BlockEntity) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof CrateBlockEntity crateBlockEntity) {
            builder = builder.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (consumer) -> {
                for(int i = 0; i < crateBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(crateBlockEntity.getItem(i));
                }
            });
        }
        return super.getDrops(blockState, builder);
    }
}
