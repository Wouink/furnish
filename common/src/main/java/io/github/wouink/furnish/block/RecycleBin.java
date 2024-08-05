package io.github.wouink.furnish.block;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.block.blockentity.RecycleBinBlockEntity;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class RecycleBin extends Block implements EntityBlock {
	private static final VoxelShape SHAPE = Block.box(2.5, 0, 2.5, 13.5, 16, 13.5);
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	private final RegistrySupplier<SoundEvent> sound;

	public RecycleBin(Properties p, RegistrySupplier<SoundEvent> sound) {
		super(p.noOcclusion());
		this.sound = sound;
		FurnishBlocks.Recycle_Bins.add(this);
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(TRIGGERED);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
		list.add(Component.translatable("block.furnish.recycle_bin.tooltip.1").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("block.furnish.recycle_bin.tooltip.2").withStyle(ChatFormatting.GRAY));
	}

	public RegistrySupplier<SoundEvent> getSound() {
		return sound;
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RecycleBinBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if(blockEntity instanceof RecycleBinBlockEntity recycleBin) {
			if(player.isCrouching()) recycleBin.empty();
			else player.openMenu(recycleBin);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return ItemInteractionResult.SUCCESS;
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if(blockEntity instanceof RecycleBinBlockEntity recycleBin) {
			if(player.isCrouching()) recycleBin.empty();
			else if(!itemStack.isEmpty()) {
				player.setItemInHand(interactionHand, recycleBin.addItem(itemStack));
				// if item was added to recycle bin, player's hand should now be empty
				if(!player.getItemInHand(interactionHand).isEmpty()) {
					player.displayClientMessage(Component.translatable("msg.furnish.recycle_bin_full"), true);
				} else level.playSound(null, blockPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0f, 1.0f);
			} else player.openMenu(recycleBin);
		}
		return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof Container) {
				Containers.dropContents(world, pos, (Container) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	// copied from DispenserBlock
	@Override
	public void neighborChanged(BlockState p_52700_, Level p_52701_, BlockPos p_52702_, Block p_52703_, BlockPos p_52704_, boolean p_52705_) {
		boolean flag = p_52701_.hasNeighborSignal(p_52702_) || p_52701_.hasNeighborSignal(p_52702_.above());
		boolean flag1 = p_52700_.getValue(TRIGGERED);
		if (flag && !flag1) {
			p_52701_.scheduleTick(p_52702_, this, 4);
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(true)), 4);
		} else if (!flag && flag1) {
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(false)), 4);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
		BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
		if(blockEntity instanceof RecycleBinBlockEntity) {
			((RecycleBinBlockEntity) blockEntity).empty();
		}
	}

	public boolean hasAnalogOutputSignal(BlockState p_52682_) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState p_52689_, Level p_52690_, BlockPos p_52691_) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_52690_.getBlockEntity(p_52691_));
	}
}
