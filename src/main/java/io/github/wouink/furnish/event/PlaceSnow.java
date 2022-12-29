package io.github.wouink.furnish.event;

import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlaceSnow {
	@SubscribeEvent
	public static void onItemUse(PlayerInteractEvent.RightClickBlock event) {
		if(event.getLevel().isClientSide()) return;
		if(event.getEntity().isShiftKeyDown()) return;

		BlockPos blockPos = event.getPos();
		if(!event.getLevel().isEmptyBlock(blockPos.above())) return;

		InteractionHand hand = event.getHand();
		ItemStack stack = event.getEntity().getItemInHand(hand);
		if(stack.getItem() != Items.SNOW) return;

		BlockState state = event.getLevel().getBlockState(blockPos);
		boolean success = true;
		if(state.getBlock() instanceof FenceBlock) {
			BlockPlaceContext ctx = new BlockPlaceContext(event.getLevel(), event.getEntity(), event.getHand(), event.getItemStack(), event.getHitVec());
			event.getLevel().setBlock(blockPos.above(), FurnishBlocks.Snow_On_Fence.get().getStateForPlacement(ctx), Block.UPDATE_ALL);
		} else if(state.getBlock() instanceof StairBlock) {
			if(state.getValue(StairBlock.HALF) == Half.BOTTOM && state.getValue(StairBlock.SHAPE) == StairsShape.STRAIGHT) {
				event.getLevel().setBlock(event.getPos().above(), FurnishBlocks.Snow_On_Stairs.get().defaultBlockState()
								.setValue(BlockStateProperties.HORIZONTAL_FACING, state.getValue(BlockStateProperties.HORIZONTAL_FACING)),
						Block.UPDATE_ALL
				);
			}
		} else success = false;

		if(success) {
			event.getEntity().swing(event.getHand());
			if(!event.getEntity().isCreative()) {
				stack.shrink(1);
				event.getEntity().setItemInHand(event.getHand(), stack);
			}
			event.getLevel().playSound(null, event.getPos(), SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		}
	}
}
