package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.blockentity.CrateBlockEntity;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.item.util.TooltipHelper;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.ArrayList;
import java.util.List;

public class Crate extends Block implements EntityBlock {
	public static ArrayList<Crate> All_Crates = new ArrayList<>();

	public Crate(Properties p) {
		super(p);
		All_Crates.add(this);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CrateBlockEntity(pos, state);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
		TooltipHelper.appendInventoryContent(itemStack, list);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity blockEntity = level.getBlockEntity(blockPos);
			if(blockEntity instanceof CrateBlockEntity crate) {
				player.openMenu(crate);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}

	// copied from ShulkerBoxBlock
	@Override
	public BlockState playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
		BlockEntity blockEntity = level.getBlockEntity(blockPos);
		if (blockEntity instanceof CrateBlockEntity crateBlockEntity) {
			if (!level.isClientSide && player.isCreative() && !crateBlockEntity.isEmpty()) {
				ItemStack itemStack = new ItemStack(this);
				itemStack.applyComponents(blockEntity.collectComponents());
				ItemEntity itemEntity = new ItemEntity(level, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, itemStack);
				itemEntity.setDefaultPickUpDelay();
				level.addFreshEntity(itemEntity);
			} else {
				crateBlockEntity.unpackLootTable(player);
			}
		}

		return super.playerWillDestroy(level, blockPos, blockState, player);
	}

	// copied from ShulkerBoxBlock
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof CrateBlockEntity crateBlockEntity) {
			params = params.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (consumer) -> {
				for(int i = 0; i < crateBlockEntity.getContainerSize(); ++i) {
					consumer.accept(crateBlockEntity.getItem(i));
				}

			});
		}
		return super.getDrops(state, params);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
		ItemStack itemStack = super.getCloneItemStack(levelReader, blockPos, blockState);
		levelReader.getBlockEntity(blockPos, FurnishRegistries.Crate_BlockEntity.get()).ifPresent((crate) -> {
			crate.saveToItem(itemStack, levelReader.registryAccess());
		});
		return itemStack;
	}
}