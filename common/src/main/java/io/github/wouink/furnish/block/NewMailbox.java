package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.blockentity.NewMailboxBlockEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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

import java.util.ArrayList;

public class NewMailbox extends HorizontalDirectionalBlock implements EntityBlock {
    public static final MapCodec<NewMailbox> CODEC = simpleCodec(NewMailbox::new);
    public static final ArrayList<NewMailbox> MAILBOXES = new ArrayList<>();
    public static final VoxelShape[] MAILBOX_SHAPE = VoxelShapeHelper.getRotatedShapes(Block.box(2, 0, 3, 14, 12, 13));
    public static final BooleanProperty ON_FENCE = BooleanProperty.create("on_fence");
    public static final BooleanProperty HAS_MAIL = BooleanProperty.create("has_mail");

    public NewMailbox(Properties properties) {
        super(properties);
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ON_FENCE, false).setValue(HAS_MAIL, false));
        MAILBOXES.add(this);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NewMailboxBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ON_FENCE, HAS_MAIL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
        if(ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof FenceBlock) {
            state = state.setValue(ON_FENCE, true);
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level, blockPos, blockState, livingEntity, itemStack);
        if(livingEntity instanceof Player player) {
            player.displayClientMessage(Component.translatable("msg.furnish.mailbox.ownership_info"), true);
        }
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return MAILBOX_SHAPE[blockState.getValue(FACING).ordinal() - 2];
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return InteractionResult.sidedSuccess(true);
        else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof NewMailboxBlockEntity mailbox) {
                if(!mailbox.hasOwner()) {
                    mailbox.setOwner(player);
                    player.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
                    return InteractionResult.sidedSuccess(false);
                } else if(mailbox.isOwner(player)) {
                    player.openMenu(mailbox);
                    return InteractionResult.sidedSuccess(false);
                } else {
                    return InteractionResult.CONSUME;
                }
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(level.isClientSide()) return ItemInteractionResult.sidedSuccess(true);
        else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof NewMailboxBlockEntity mailbox) {
                if(!mailbox.hasOwner()) {
                    mailbox.setOwner(player);
                    player.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
                    return ItemInteractionResult.sidedSuccess(false);
                } else if (mailbox.isOwner(player)) {
                    player.openMenu(mailbox);
                    return ItemInteractionResult.sidedSuccess(false);
                } else if (itemStack.isEmpty()) {
                    player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
                    return ItemInteractionResult.CONSUME;
                } else if (mailbox.isFull()) {
                    player.displayClientMessage(Component.translatable("msg.furnish.mailbox.full"), true);
                    return ItemInteractionResult.CONSUME;
                } else {
                    ItemStack result = mailbox.insertMail(itemStack);
                    player.setItemInHand(interactionHand, result);
                    if(result.isEmpty()) {
                        Component ownerDisplayName = mailbox.getOwnerDisplayName();
                        if(ownerDisplayName != null) {
                            player.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered_to", ownerDisplayName), true);
                        } else {
                            player.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered"), true);
                        }
                        return ItemInteractionResult.sidedSuccess(false);
                    } else {
                        player.displayClientMessage(Component.translatable("msg.furnish.mailbox.invalid_mail"), true);
                        return ItemInteractionResult.CONSUME;
                    }
                }
            } else {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
    }

    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        super.onRemove(blockState, level, blockPos, blockState2, bl);
        if(blockState.getBlock() != blockState2.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof NewMailboxBlockEntity mailbox) {
                Containers.dropContents(level, blockPos, mailbox);
            }
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        if(!level.isClientSide()) {
            if(blockEntity instanceof NewMailboxBlockEntity mailbox) {
                if(mailbox.isOwner(player) || (player.isCreative() && (player.hasPermissions(1) || blockState.is(FurnishRegistries.NON_OP_CREATIVE_CAN_DESTROY_TAG)))) {
                    super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
                } else {
                    player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
                }
            }
        }
    }
}
