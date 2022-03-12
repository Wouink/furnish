package io.github.wouink.furnish.event;

import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class GivePlateToEnderman {

	@SubscribeEvent
	public static void onMobSpawn(EntityJoinWorldEvent event) {
		if(event.getWorld().isClientSide() || event.loadedFromDisk()) return;

		if(!(event.getEntity() instanceof EnderMan)) return;

		EnderMan enderman = (EnderMan) event.getEntity();
		if(enderman.getCarriedBlock() != null) return;

		Random rand = event.getWorld().getRandom();
		if(rand.nextInt(100) == 10) {
			enderman.setCarriedBlock(FurnishBlocks.Rare_Plates.get(new Random().nextInt(FurnishBlocks.Rare_Plates.size())).get().defaultBlockState());
		}
	}
}
