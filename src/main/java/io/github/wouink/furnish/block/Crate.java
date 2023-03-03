package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.CrateTileEntity;
import io.github.wouink.furnish.item.util.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Crate extends Block implements EntityBlock {
	public static ArrayList<Crate> All_Crates = new ArrayList<>();

	public Crate(Properties p) {
		super(p);
		All_Crates.add(this);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrateTileEntity(pos, state);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		TooltipHelper.appendInventoryContent(stack, tooltip);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult result) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof CrateTileEntity) {
				playerEntity.openMenu((MenuProvider) tileEntity);
			}
			return InteractionResult.CONSUME;
		}
	}

	// copied from ShulkerBoxBlock
	public void playerWillDestroy(Level p_56212_, BlockPos p_56213_, BlockState p_56214_, Player p_56215_) {
		BlockEntity blockentity = p_56212_.getBlockEntity(p_56213_);
		if (blockentity instanceof CrateTileEntity) {
			CrateTileEntity shulkerboxblockentity = (CrateTileEntity) blockentity;
			if (!p_56212_.isClientSide && p_56215_.isCreative() && !shulkerboxblockentity.isEmpty()) {
				ItemStack itemstack = new ItemStack(this);
				blockentity.saveToItem(itemstack);
				if (shulkerboxblockentity.hasCustomName()) {
					itemstack.setHoverName(shulkerboxblockentity.getCustomName());
				}

				ItemEntity itementity = new ItemEntity(p_56212_, (double)p_56213_.getX() + 0.5D, (double)p_56213_.getY() + 0.5D, (double)p_56213_.getZ() + 0.5D, itemstack);
				itementity.setDefaultPickUpDelay();
				p_56212_.addFreshEntity(itementity);
			} else {
				shulkerboxblockentity.unpackLootTable(p_56215_);
			}
		}

		super.playerWillDestroy(p_56212_, p_56213_, p_56214_, p_56215_);
	}

	// copied from ShulkerBoxBlock
	public List<ItemStack> getDrops(BlockState p_56246_, LootContext.Builder p_56247_) {
		BlockEntity blockentity = p_56247_.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockentity instanceof CrateTileEntity) {
			CrateTileEntity shulkerboxblockentity = (CrateTileEntity) blockentity;
			p_56247_ = p_56247_.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (p_56218_, p_56219_) -> {
				for(int i = 0; i < shulkerboxblockentity.getContainerSize(); ++i) {
					p_56219_.accept(shulkerboxblockentity.getItem(i));
				}

			});
		}

		return super.getDrops(p_56246_, p_56247_);
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if(stack.hasCustomHoverName()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof CrateTileEntity) {
				((CrateTileEntity) tileEntity).setCustomName(stack.getHoverName());
			}
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.DESTROY;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(world, pos, state);
		CrateTileEntity crate = (CrateTileEntity) world.getBlockEntity(pos);
		CompoundTag nbt = crate.saveToTag(new CompoundTag());
		if(!nbt.isEmpty()) stack.addTagElement("BlockEntityTag", nbt);
		return stack;
	}
}