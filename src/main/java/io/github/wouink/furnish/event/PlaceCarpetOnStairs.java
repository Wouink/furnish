package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.*;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlaceCarpetOnStairs {

	@SubscribeEvent
	public void onCarpetPlaced(BlockEvent.EntityPlaceEvent event) {
		if(event.getPlacedBlock().getBlock() instanceof CarpetBlock) {
			BlockState stateBelow = event.getWorld().getBlockState(event.getPos().below());
			if(stateBelow.getBlock() instanceof StairsBlock) {
				if(stateBelow.getValue(StairsBlock.HALF) == Half.BOTTOM && stateBelow.getValue(StairsBlock.SHAPE) == StairsShape.STRAIGHT) {
					String color = ((CarpetBlock) event.getPlacedBlock().getBlock()).getColor().getName();
					event.getWorld().setBlock(
							event.getPos(),
							FurnishManager.Carpets_On_Stairs.get(color).defaultBlockState().setValue(HorizontalBlock.FACING, stateBelow.getValue(HorizontalBlock.FACING)),
							3
					);
				}
			}
		}
	}
}