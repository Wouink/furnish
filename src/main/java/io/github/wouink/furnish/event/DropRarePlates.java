package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DropRarePlates {

	@SubscribeEvent
	public void onZombieDrops(LivingDropsEvent event) {
		if(event.getEntityLiving() instanceof ZombieEntity && FurnishManager.Plates.length > 17) {
			World world = event.getEntityLiving().level;
			if(world.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
				System.out.println(event.getLootingLevel());
				if(world.getRandom().nextInt(100) <= event.getLootingLevel()) {
					int plate = world.getRandom().nextInt(FurnishManager.Plates.length - 17);
					System.out.println("plate = " + plate);
					BlockPos pos = event.getEntityLiving().blockPosition();
					event.getDrops().add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FurnishManager.Plates[17 + plate])));
				}
			}
		}
	}
}
