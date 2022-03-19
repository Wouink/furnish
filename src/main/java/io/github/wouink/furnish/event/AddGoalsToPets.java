package io.github.wouink.furnish.event;

import io.github.wouink.furnish.entity.ai.LieInBasketGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddGoalsToPets {

	@SubscribeEvent
	public static void onPetJoin(EntityJoinWorldEvent event) {
		if(event.getEntity() instanceof Cat cat) {
			cat.goalSelector.addGoal(0, new LieInBasketGoal.CatGoal(cat, 1.1d, 10, 6));
		} else if(event.getEntity() instanceof Wolf wolf) {
			wolf.goalSelector.addGoal(0, new LieInBasketGoal(wolf, 1.1d, 10, 6));
		}
	}
}
