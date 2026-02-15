package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.blockentity.MailboxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * IMPORTANT - on server, spawn protection prevents interaction with the mailbox (no message is ever displayed)
 */
public class Mailbox extends AbstractStorageFurnitureBlock {
    public static final VoxelShape[] MAILBOX_SHAPE = ShapeHelper.getRotatedShapes(Block.box(2, 0, 3, 14, 12, 13));
    public static final BooleanProperty ON_FENCE = BooleanProperty.create("on_fence");
    public static final BooleanProperty HAS_MAIL = BooleanProperty.create("has_mail");

    public Mailbox(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(HAS_MAIL, false).setValue(ON_FENCE, false));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Mailbox::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MailboxBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        // FACING handled by superclass
        builder.add(ON_FENCE, HAS_MAIL);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
        if(blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().below()).getBlock() instanceof FenceBlock) {
            blockState = blockState.setValue(ON_FENCE, true);
        }
        return blockState;
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return MAILBOX_SHAPE[blockState.getValue(FACING).ordinal() - 2];
    }

    // refuse destruction if not owner/not creative op player
    // this could, in theory, be handled by playerWillDestroy, but the mailbox is always destroyed... no matter what
    // so let's use events instead (see FurnishContents.init for registration)
    public static boolean beforeBreakingMailbox(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity) {
        // level.isClientSide() is always false here, this code only runs on server (this is what we want!)

        if(!(blockState.getBlock() instanceof Mailbox)) return true; // pass to other event listeners
        if(!(blockEntity != null && blockEntity instanceof MailboxBlockEntity mailbox)) return true;

        boolean adminDestroy = player.canUseGameMasterBlocks(); // = creative + op
        if(adminDestroy) Furnish.LOGGER.info("Mailbox at {} destroyed by admin {}", blockPos, player.getName().getString());

        if(mailbox.isOwner(player) || adminDestroy) return true;

        player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
        return false; // stop there and refuse to destroy the mailbox
    }

    // notify about ownership upon placing
    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if(livingEntity instanceof Player player) {
            player.displayClientMessage(Component.translatable("msg.furnish.mailbox.ownership_info"), true);
        }
    }

    // process the mailbox login on right-click
    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        return useItemOn(ItemStack.EMPTY, blockState, level, blockPos, player, InteractionHand.MAIN_HAND, blockHitResult);
    }

    // process the mailbox login on right-click
    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof MailboxBlockEntity mailbox) {
            if(!mailbox.hasOwner()) {
                mailbox.setOwner(player);
                player.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
                return InteractionResult.SUCCESS;
            }
            if(mailbox.isOwner(player)) {
                player.openMenu(mailbox);
                return InteractionResult.SUCCESS;
            }
            if(itemStack.isEmpty()) {
                player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
                return InteractionResult.CONSUME;
            }
            if(mailbox.isFull()) {
                player.displayClientMessage(Component.translatable("msg.furnish.mailbox.full"), true);
                return InteractionResult.CONSUME;
            }
            ItemStack result = mailbox.insertMail(itemStack);
            player.setItemInHand(interactionHand, result);
            // mail was delivered
            if(result.isEmpty()) {
                String translationKey = "msg.furnish.mailbox.mail_delivered";
                Component ownerDisplayName = mailbox.getOwnerDisplayName();
                Component message;
                if(ownerDisplayName != null) message = Component.translatable(translationKey + "_to", ownerDisplayName);
                else message = Component.translatable(translationKey);
                player.displayClientMessage(message, true);
                return InteractionResult.SUCCESS;
            }
            // invalid mail
            player.displayClientMessage(Component.translatable("msg.furnish.mailbox.invalid_mail"), true);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
