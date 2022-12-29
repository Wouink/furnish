package io.github.wouink.furnish.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class PopLecternBook {

	// Port this Bedrock functionality to Java.
	public static void onBlockClick(PlayerInteractEvent.LeftClickBlock event) {
		System.out.println("click!");
		if(event.getLevel().isClientSide()) return;
		BlockState state = event.getLevel().getBlockState(event.getPos());
		if(state.getBlock() instanceof LecternBlock) {
			if(state.getValue(LecternBlock.HAS_BOOK)) {
				popBook(state, event.getLevel(), event.getPos());
				// event.getLevel().sendBlockUpdated(event.getPos(), state, state, Block.UPDATE_ALL);
			}
		}
	}

	// copied from LecternBlock
	private static void popBook(BlockState p_54588_, Level p_54589_, BlockPos p_54590_) {
		BlockEntity blockentity = p_54589_.getBlockEntity(p_54590_);
		if (blockentity instanceof LecternBlockEntity lecternblockentity) {
			Direction direction = p_54588_.getValue(LecternBlock.FACING);
			ItemStack itemstack = lecternblockentity.getBook().copy();
			float f = 0.25F * (float)direction.getStepX();
			float f1 = 0.25F * (float)direction.getStepZ();
			ItemEntity itementity = new ItemEntity(p_54589_, (double)p_54590_.getX() + 0.5D + (double)f, (double)(p_54590_.getY() + 1), (double)p_54590_.getZ() + 0.5D + (double)f1, itemstack);
			itementity.setDefaultPickUpDelay();
			p_54589_.addFreshEntity(itementity);
			lecternblockentity.clearContent();
		}

	}
}
