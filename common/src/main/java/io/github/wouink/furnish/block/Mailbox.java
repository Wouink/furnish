package io.github.wouink.furnish.block;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Mailbox extends HorizontalDirectionalBlock implements EntityBlock {
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, ON_FENCE, HAS_MAIL);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		if(ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof FenceBlock) {
			state = state.setValue(ON_FENCE, true);
		}
		return state;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if(entity instanceof Player) {
			((Player) entity).displayClientMessage(Component.translatable("msg.furnish.mailbox.ownership_info"), true);
		}
		if(stack.hasCustomHoverName()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				((MailboxTileEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return MAILBOX_SHAPE[state.getValue(FACING).ordinal() - 2];
	}

	private boolean updateMailbox(BlockState state, Level world, BlockPos pos) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof MailboxTileEntity) {
			boolean mail = ((MailboxTileEntity) tileEntity).hasMail();
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
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.sidedSuccess(true);
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(!(tileEntity instanceof MailboxTileEntity)) return InteractionResult.FAIL;
			MailboxTileEntity mailbox = (MailboxTileEntity) tileEntity;

			if(!mailbox.hasOwner()) {
				Furnish.LOG.debug("Mailbox does not have an owner -- setting owner and quitting");
				mailbox.setOwner(playerEntity);
				playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.set_owner"), true);
				return InteractionResult.sidedSuccess(false);
			}

			Furnish.LOG.debug("Mailbox has an owner -- continuing");

			if(mailbox.isOwner(playerEntity)) {
				Furnish.LOG.debug("Player is the owner -- updating state or opening UI, and quitting");
				mailbox.updateDisplayName(playerEntity);
				if(!updateMailbox(state, world, pos)) playerEntity.openMenu(mailbox);
				return InteractionResult.sidedSuccess(false);
			}

			Furnish.LOG.debug("Player is not the owner -- continuing");

			if(playerEntity.getItemInHand(hand).isEmpty()) {
				Furnish.LOG.debug("Player has no item in hand -- quitting");
				playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
				return InteractionResult.FAIL;
			}

			Furnish.LOG.debug("Player has an item in hand -- continuing");

			if(mailbox.isFull()) {
				Furnish.LOG.debug("Mailbox is full -- quitting");
				playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.full"), true);
				return InteractionResult.FAIL;
			}

			Furnish.LOG.debug("Mailbox is not full -- continuing");

			ItemStack result = mailbox.addMail(playerEntity.getItemInHand(hand));
			playerEntity.setItemInHand(hand, result);
			updateMailbox(state, world, pos);

			if(result.isEmpty()) {
				Furnish.LOG.debug("Mail is added to mailbox -- quitting");
				Component ownerName = mailbox.getOwnerDisplayName();
				if (ownerName != null) {
					playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered_to", ownerName), true);
				} else {
					playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.mail_delivered"), true);
				}
				return InteractionResult.sidedSuccess(false);
			}

			Furnish.LOG.debug("Mail is not added to mailbox -- end of method");

			playerEntity.displayClientMessage(Component.translatable("msg.furnish.mailbox.invalid_mail"), true);
			return InteractionResult.FAIL;
		}
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MailboxTileEntity(pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				Containers.dropContents(world, pos, (MailboxTileEntity) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
		if(!level.isClientSide()) {
			if(blockEntity != null) {
				if(blockEntity instanceof MailboxTileEntity mailboxTileEntity) {
					if(mailboxTileEntity.isOwner(player) || (player.isCreative() && (player.hasPermissions(1) || blockState.is(FurnishRegistries.NON_OP_CREATIVE_CAN_DESTROY_TAG)))) {
						super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
					} else {
						player.displayClientMessage(Component.translatable("msg.furnish.mailbox.no_permission"), true);
					}
				}
			}
		}
	}
}
