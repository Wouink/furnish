package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import io.github.wouink.furnish.block.tileentity.CrateTileEntity;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.item.util.TooltipHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Crate extends Block implements ISpecialItemProperties {
	public Crate(Properties p, String registryName) {
		super(p);
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CrateTileEntity();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.appendHoverText(stack, world, tooltip, flag);
		TooltipHelper.appendInventoryContent(stack, tooltip);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
		if(world.isClientSide()) return ActionResultType.SUCCESS;
		else if(playerEntity.isSpectator()) return ActionResultType.CONSUME;
		else {
			TileEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof CrateTileEntity) {
				playerEntity.openMenu((CrateTileEntity) tileEntity);
				return ActionResultType.CONSUME;
			} else return ActionResultType.PASS;
		}
	}

	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity playerEntity) {
		TileEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof CrateTileEntity) {
			CrateTileEntity crate = (CrateTileEntity) tileEntity;
			if(!world.isClientSide() && playerEntity.isCreative() && !crate.isEmpty()) {
				ItemStack stack = new ItemStack(this);
				CompoundNBT nbt = crate.save(new CompoundNBT());
				if(!nbt.isEmpty()) stack.addTagElement("BlockEntityTag", nbt);
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
		TileEntity tileEntity = builder.getOptionalParameter(LootParameters.BLOCK_ENTITY);
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
	public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		if(stack.hasCustomHoverName()) {
			TileEntity tileEntity = world.getBlockEntity(pos);
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
	public ItemStack getCloneItemStack(IBlockReader world, BlockPos pos, BlockState state) {
		ItemStack stack = super.getCloneItemStack(world, pos, state);
		CrateTileEntity crate = (CrateTileEntity) world.getBlockEntity(pos);
		CompoundNBT nbt = crate.saveToTag(new CompoundNBT());
		if(!nbt.isEmpty()) stack.addTagElement("BlockEntityTag", nbt);
		return stack;
	}

	@Override
	public Item.Properties getProperties() {
		return new Item.Properties().stacksTo(1).tab(FurnishManager.Furnish_ItemGroup);
	}
}