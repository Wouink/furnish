package io.github.wouink.furnish.event;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.entity.ai.LieInBasketGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddGoalsToPets {

	@SubscribeEvent
	public static void onPetJoin(EntityJoinLevelEvent event) {
		if(event.getLevel().isClientSide()) return;
		if(event.getEntity() instanceof Cat cat) {
			Furnish.debug("adding goal to cat " + cat);
			cat.goalSelector.addGoal(0, new LieInBasketGoal.CatLieInBasketGoal(cat, 1.1d, 10, 6));
			cat.goalSelector.getAvailableGoals().forEach((goal) -> Furnish.debug(goal.getGoal().toString()));
		} else if(event.getEntity() instanceof Wolf wolf) {
			Furnish.debug("adding goal to wolf " + wolf);
			wolf.goalSelector.addGoal(0, new LieInBasketGoal(wolf, 1.1d, 10, 6));
			wolf.goalSelector.getAvailableGoals().forEach((goal) -> Furnish.debug(goal.getGoal().toString()));
		}
	}
}
