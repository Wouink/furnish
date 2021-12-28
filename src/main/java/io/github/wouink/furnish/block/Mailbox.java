package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Mailbox extends HorizontalBlock {
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
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, ON_FENCE, HAS_MAIL);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		if(ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() instanceof FenceBlock) {
			state = state.setValue(ON_FENCE, true);
		}
		return state;
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if(entity instanceof PlayerEntity) {
			((PlayerEntity) entity).displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.ownership_info"), true);
		}
		if(stack.hasCustomHoverName()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				((MailboxTileEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return MAILBOX_SHAPE[state.getValue(FACING).ordinal() - 2];
	}

	private boolean updateMailbox(BlockState state, World world, BlockPos pos) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof MailboxTileEntity) {
			boolean mail = ((MailboxTileEntity) tileEntity).hasMail();
			if(state.getValue(HAS_MAIL).booleanValue() != mail) {
				world.setBlock(pos, state.setValue(HAS_MAIL, mail), 3);
				tileEntity.setChanged();
				world.playSound(null, pos, FurnishData.Sounds.Mailbox_Update.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
				return true;
			}
		}
		return false;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult rayTraceResult) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;

		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof MailboxTileEntity) {
			MailboxTileEntity mailbox = (MailboxTileEntity) tileEntity;

			if(!mailbox.hasOwner()) {
				mailbox.setOwner(playerEntity);
				playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.set_owner"), true);
				return ActionResultType.CONSUME;
			}

			if(mailbox.isOwner(playerEntity)) {
				mailbox.updateDisplayName(playerEntity);
				if(!updateMailbox(state, world, pos)) playerEntity.openMenu(mailbox);
				return ActionResultType.CONSUME;
			}

			if(playerEntity.getItemInHand(hand).isEmpty()) {
				playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.no_permission"), true);
				return ActionResultType.FAIL;
			}

			if(mailbox.isFull()) {
				playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.full"), true);
				return ActionResultType.FAIL;
			}

			ItemStack result = mailbox.addMail(playerEntity.getItemInHand(hand));
			playerEntity.setItemInHand(hand, result);
			updateMailbox(state, world, pos);
			if(result.isEmpty()) {
				ITextComponent ownerName = mailbox.getOwnerDisplayName();
				if(ownerName != null) {
					playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.mail_delivered_to", ownerName), true);
				} else {
					playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.mail_delivered"), true);
				}

				return ActionResultType.CONSUME;
			} else {
				playerEntity.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.invalid_mail"), true);
				return ActionResultType.FAIL;
			}
		}

		return ActionResultType.FAIL;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new MailboxTileEntity();
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof IInventory) {
				InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest, FluidState fluid) {
		if(!world.isClientSide()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof MailboxTileEntity) {
				if(((MailboxTileEntity) tileEntity).isOwner(player)) {
					return super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
				} else {
					player.displayClientMessage(new TranslationTextComponent("msg.furnish.mailbox.no_permission"), true);
				}
			}
		}
		return false;
	}
}
