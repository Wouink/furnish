package io.github.wouink.furnish.event;

import io.github.wouink.furnish.block.CarpetOnTrapdoor;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WoolCarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PlaceCarpet {

	@SubscribeEvent
	public static void onCarpetPlaced(BlockEvent.EntityPlaceEvent event) {
		if(event.getLevel().isClientSide()) return;
		if(event.getPlacedBlock().getBlock() instanceof WoolCarpetBlock) {
			// only replace vanilla carpets
			if(!ForgeRegistries.BLOCKS.getKey(event.getPlacedBlock().getBlock()).getNamespace().equals("minecraft")) return;

			BlockState stateBelow = event.getLevel().getBlockState(event.getPos().below());
			if(stateBelow.getBlock() instanceof StairBlock && !event.getEntity().isShiftKeyDown()) {
				if(stateBelow.getValue(StairBlock.HALF) == Half.BOTTOM && stateBelow.getValue(StairBlock.SHAPE) == StairsShape.STRAIGHT) {
					String color = ((WoolCarpetBlock) event.getPlacedBlock().getBlock()).getColor().getName();
					event.getLevel().setBlock(
							event.getPos(),
							FurnishBlocks.Carpets_On_Stairs.get(color).get().defaultBlockState().setValue(
									BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING)),
							Block.UPDATE_ALL
					);
				}
			} else if(stateBelow.getBlock() instanceof TrapDoorBlock) {
				if(stateBelow.getValue(TrapDoorBlock.HALF) == Half.TOP) {
					String color = ((WoolCarpetBlock) event.getPlacedBlock().getBlock()).getColor().getName();
					event.getLevel().setBlock(
							event.getPos(),
							FurnishBlocks.Carpets_On_Trapdoors.get(color).get().defaultBlockState().setValue(
									BlockStateProperties.HORIZONTAL_FACING, stateBelow.getValue(BlockStateProperties.HORIZONTAL_FACING))
									.setValue(CarpetOnTrapdoor.OPEN, stateBelow.getValue(TrapDoorBlock.OPEN)),
							Block.UPDATE_ALL
					);
				}
			}
		}
	}
}
