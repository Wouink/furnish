package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.CrateTileEntity;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.item.util.TooltipHelper;
import io.github.wouink.furnish.setup.FurnishItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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

public class Crate extends Block implements EntityBlock, ISpecialItemProperties {
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
		else if(playerEntity.isSpectator()) return InteractionResult.CONSUME;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof CrateTileEntity) {
				playerEntity.openMenu((CrateTileEntity) tileEntity);
				return InteractionResult.CONSUME;
			} else return InteractionResult.PASS;
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player playerEntity) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof CrateTileEntity) {
			CrateTileEntity crate = (CrateTileEntity) tileEntity;
			if(!world.isClientSide() && playerEntity.isCreative() && !crate.isEmpty()) {
				ItemStack stack = new ItemStack(this);
				crate.saveToItem(stack);
				if(crate.hasCustomName()) stack.setHoverName(crate.getCustomName());
				ItemEntity item = new ItemEntity(world, (double)pos.getX() + .5, (double)pos.getZ() + .5, (double)pos.getZ() + .5, stack);
				item.setDefaultPickUpDelay();
				world.addFreshEntity(item);
			} else {
				crate.unpackLootTable(playerEntity);
			}
		}
		super.playerWillDestroy(world, pos, state, playerEntity);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		BlockEntity tileEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if(tileEntity instanceof CrateTileEntity) {
			CrateTileEntity crate = (CrateTileEntity) tileEntity;
			builder = builder.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (lootCtx, stackConsumer) -> {
				for(int i = 0; i < crate.getContainerSize(); i++) {
					stackConsumer.accept(crate.getItem(i));
				}
			});
		}
		return super.getDrops(state, builder);
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

	@Override
	public Item.Properties getProperties() {
		return new Item.Properties().stacksTo(1).tab(FurnishItems.Furnish_ItemGroup);
	}
}