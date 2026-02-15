package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * When harvested, the Crate block drops as an item containing all of its contents.
 * This is handled by loot tables rather than by code.
 * The loot table is generated using `createShulkerBoxDrop` in the datagen code.
 *
 * The Crate is not a directional block, this is why it is not a subclass of AbstractStorageFurnitureBlock.
 */
public class Crate extends Block implements EntityBlock {
    public Crate(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrateBlockEntity(blockPos, blockState);
    }

    /* TODO ensure tooltip is still here
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        TooltipHelper.appendInventoryContents(itemStack, list);
    }
     */

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        level.getBlockEntity(blockPos, FurnishContents.CRATE_BLOCK_ENTITY).ifPresent(
                crate -> player.openMenu(crate)
        );
        return InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return useWithoutItem(blockState, level, blockPos, player, blockHitResult);
    }

    // copies the content on mouse wheel click
    @Override
    public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        ItemStack result = super.getCloneItemStack(levelReader, blockPos, blockState, bl);
        levelReader.getBlockEntity(blockPos, FurnishContents.CRATE_BLOCK_ENTITY).ifPresent(
                crateBlockEntity -> {
                    result.applyComponents(crateBlockEntity.collectComponents());
                }
        );
        return result;
    }
}
