package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.blockentity.RecycleBinBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RecycleBin extends AbstractStorageFurnitureBlock {
    private static final VoxelShape SHAPE = Block.box(2.5, 0, 2.5, 13.5, 16, 13.5);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    private SoundEvent sound;

    public RecycleBin(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TRIGGERED);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(RecycleBin::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new RecycleBinBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    /* TODO
    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        list.add(Component.translatable("block.furnish.recycle_bin.tooltip.1").withStyle(ChatFormatting.GRAY));
        list.add(Component.translatable("block.furnish.recycle_bin.tooltip.2").withStyle(ChatFormatting.GRAY));
    }
    */

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        level.getBlockEntity(blockPos, FurnishContents.RECYCLE_BIN_BLOCK_ENTITY).ifPresent(recycleBin -> {
            if(player.isCrouching()) {
                if(recycleBin.empty())
                    player.displayClientMessage(Component.translatable("msg.furnish.recycle_bin_empty"), true);
            } else if(itemStack.isEmpty()) player.openMenu(recycleBin);
            else {
                ItemStack ret = recycleBin.addItem(itemStack);
                player.setItemInHand(interactionHand, ret);
                if(!ret.isEmpty())
                    player.displayClientMessage(Component.translatable("msg.furnish.recycle_bin_full"), true);
                else
                    level.playSound(null, blockPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
            }
        });
        return InteractionResult.CONSUME;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        return useItemOn(ItemStack.EMPTY, blockState, level, blockPos, player, InteractionHand.MAIN_HAND, blockHitResult);
    }

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    public SoundEvent getSound() {
        return sound;
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean bl) {
        boolean powered = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
        boolean triggered = blockState.getValue(TRIGGERED);
        if(powered && !triggered) {
            level.scheduleTick(blockPos, this, Block.UPDATE_INVISIBLE);
            level.setBlock(blockPos, blockState.setValue(TRIGGERED, true), Block.UPDATE_INVISIBLE);
        } else if(!powered && triggered) {
            level.setBlock(blockPos, blockState.setValue(TRIGGERED, false), UPDATE_INVISIBLE);
        }
    }

    @Override
    protected void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        serverLevel.getBlockEntity(blockPos, FurnishContents.RECYCLE_BIN_BLOCK_ENTITY).ifPresent(recycleBin -> {
            recycleBin.empty();
        });
    }
}
