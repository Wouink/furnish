package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.level.material.FluidState;
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
			((Player) entity).displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.ownership_info"), true);
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
				world.setBlock(pos, state.setValue(HAS_MAIL, mail), 3);
				tileEntity.setChanged();
				world.playSound(null, pos, FurnishData.Sounds.Mailbox_Update.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
				return true;
			}
		}
		return false;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		InteractionResult res = InteractionResult.FAIL;

		if (!world.isClientSide()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				MailboxTileEntity mailbox = (MailboxTileEntity) tileEntity;

				if(!mailbox.hasOwner()) {
					mailbox.setOwner(playerEntity);
					playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.set_owner"), true);
					res = InteractionResult.SUCCESS;
				}

				else if(mailbox.isOwner(playerEntity)) {
					mailbox.updateDisplayName(playerEntity);
					if(!updateMailbox(state, world, pos)) playerEntity.openMenu(mailbox);
					res = InteractionResult.SUCCESS;
				}

				else if(playerEntity.getItemInHand(hand).isEmpty()) {
					playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.no_permission"), true);
					res = InteractionResult.FAIL;
				}

				else if(mailbox.isFull()) {
					playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.full"), true);
					res = InteractionResult.FAIL;
				}

				else {
					ItemStack result = mailbox.addMail(playerEntity.getItemInHand(hand));
					playerEntity.setItemInHand(hand, result);
					updateMailbox(state, world, pos);
					if(result.isEmpty()) {
						Component ownerName = mailbox.getOwnerDisplayName();
						if(ownerName != null) {
							playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.mail_delivered_to", ownerName), true);
						} else {
							playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.mail_delivered"), true);
						}

						res = InteractionResult.SUCCESS;
					} else {
						playerEntity.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.invalid_mail"), true);
						res = InteractionResult.FAIL;
					}
				}
			}
		}

		return res == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(world.isClientSide()) : res;
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
	public boolean onDestroyedByPlayer(BlockState state, Level world, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		if(!world.isClientSide()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				if(((MailboxTileEntity) tileEntity).isOwner(player)) {
					return super.onDestroyedByPlayer(state, world, pos, player, willHarvest, fluid);
				} else {
					player.displayClientMessage(new TranslatableComponent("msg.furnish.mailbox.no_permission"), true);
				}
			}
		}
		return false;
	}
}
