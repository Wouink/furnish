package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.blockentity.MailboxBlockEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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


import java.util.ArrayList;

public class Mailbox extends HorizontalDirectionalBlock implements EntityBlock {
	public static final MapCodec<Mailbox> CODEC = simpleCodec(Mailbox::new);
	public static ArrayList<Mailbox> All_Mailboxes = new ArrayList<>();
	
	public static final VoxelShape[] MAILBOX_SHAPE = VoxelShapeHelper.getRotatedShapes(Block.box(2, 0, 3, 14, 12, 13));
	public static final BooleanProperty ON_FENCE = BooleanProperty.create("on_fence");
	public static final BooleanProperty HAS_MAIL = BooleanProperty.create("has_mail");
	public Mailbox(Properties p) {
		super(p);
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ON_FENCE, false).setValue(HAS_MAIL, false));
		All_Mailboxes.add(this);
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
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
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
		if(entity instanceof Player player) {
			player.displayClientMessage(Component.translatable("msg.furnish.mailbox.ownership_info"), true);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return MAILBOX_SHAPE[state.getValue(FACING).ordinal() - 2];
	}

	private boolean updateMailbox(BlockState state, Level world, BlockPos pos) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof MailboxBlockEntity) {
			boolean mail = ((MailboxBlockEntity) tileEntity).hasMail();
			if(state.getValue(HAS_MAIL).booleanValue() != mail) {
				world.setBlock(pos, state.setValue(HAS_MAIL, mail), Block.UPDATE_ALL);
				tileEntity.setChanged();
				world.playSound(null, pos, FurnishRegistries.Mailbox_Update_Sound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				return true;
			}
		}
		return false;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.sidedSuccess(true);
		else {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if(blockEntity instanceof MailboxBlockEntity mailboxBlockEntity) {
				if(!mailboxBlockEntity.hasOwner()) {
					Furnish.LOG.debug("Mailbox does not have an owner -- setting owner and quitting");
					mailboxBlockEntity.setOwner(player);
					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
					return InteractionResult.sidedSuccess(false);
				} else if(mailboxBlockEntity.isOwner(player)) {
					Furnish.LOG.debug("Player is the owner -- updating state or opening UI, and quitting");
					mailboxBlockEntity.updateDisplayName(player);
					if(!updateMailbox(blockState, level, blockPos)) player.openMenu(mailboxBlockEntity);
					return InteractionResult.sidedSuccess(false);
				} else {
					Furnish.LOG.debug("Player is not the owner -- refusing access");
					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
					return InteractionResult.FAIL;
				}
			} else return InteractionResult.FAIL;
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return ItemInteractionResult.sidedSuccess(true);
		else {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if(blockEntity instanceof MailboxBlockEntity mailboxBlockEntity) {
				if(!mailboxBlockEntity.hasOwner()) {
					Furnish.LOG.debug("Mailbox does not have an owner -- setting owner and quitting");
					mailboxBlockEntity.setOwner(player);
					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
					return ItemInteractionResult.sidedSuccess(false);
				} else if(mailboxBlockEntity.isOwner(player)) {
					Furnish.LOG.debug("Player is the owner -- updating state or opening UI, and quitting");
					mailboxBlockEntity.updateDisplayName(player);
					if(!updateMailbox(blockState, level, blockPos)) player.openMenu(mailboxBlockEntity);
					return ItemInteractionResult.sidedSuccess(false);
				} else if(itemStack.isEmpty()) {
					Furnish.LOG.debug("Player is not the owner -- refusing access");
					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
					return ItemInteractionResult.FAIL;
				} else if(mailboxBlockEntity.isFull()) {
					Furnish.LOG.debug("Mailbox is full -- quitting");
					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.full"), true);
					return ItemInteractionResult.FAIL;
				} else {
					Furnish.LOG.debug("Mailbox is not full -- continuing");

					ItemStack result = mailboxBlockEntity.addMail(itemStack);
					player.setItemInHand(interactionHand, result);
					updateMailbox(blockState, level, blockPos);

					if(result.isEmpty()) {
						Furnish.LOG.debug("Mail is added to mailbox -- quitting");
						Component ownerName = mailboxBlockEntity.getOwnerDisplayName();
						if (ownerName != null) {
							player.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered_to", ownerName), true);
						} else {
							player.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered"), true);
						}
						return ItemInteractionResult.sidedSuccess(false);
					}

					Furnish.LOG.debug("Mail is not added to mailbox -- end of method");

					player.displayClientMessage(Component.translatable("msg.furnish.mailbox.invalid_mail"), true);
					return ItemInteractionResult.FAIL;
				}
			} else return ItemInteractionResult.FAIL;
		}
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MailboxBlockEntity(pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxBlockEntity) {
				Containers.dropContents(world, pos, (MailboxBlockEntity) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack) {
		if(!level.isClientSide()) {
			if(blockEntity != null) {
				if(blockEntity instanceof MailboxBlockEntity mailboxBlockEntity) {
					if(mailboxBlockEntity.isOwner(player) || (player.isCreative() && (player.hasPermissions(1) || blockState.is(FurnishRegistries.NON_OP_CREATIVE_CAN_DESTROY_TAG)))) {
						super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
					} else {
						player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
					}
				}
			}
		}
	}
}
