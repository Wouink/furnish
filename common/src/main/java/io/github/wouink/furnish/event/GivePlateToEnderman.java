package io.github.wouink.furnish.event;

import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class GivePlateToEnderman {

	@SubscribeEvent
	public static void onMobSpawn(EntityJoinLevelEvent event) {
		if(event.getLevel().isClientSide() || event.loadedFromDisk()) return;

		if(!(event.getEntity() instanceof EnderMan)) return;

		EnderMan enderman = (EnderMan) event.getEntity();
		if(enderman.getCarriedBlock() != null) return;

		RandomSource rand = event.getLevel().getRandom();
		if(rand.nextInt(100) == 10) {
			enderman.setCarriedBlock(FurnishBlocks.Rare_Plates.get(new Random().nextInt(FurnishBlocks.Rare_Plates.size())).get().defaultBlockState());
		}
	}
}
