package io.github.wouink.furnish.event;

import io.github.wouink.furnish.block.Coffin;
import net.minecraftforge.event.entity.player.SleepingTimeCheckEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SleepInCoffin {

	@SubscribeEvent
	public static void checkSleepTime(SleepingTimeCheckEvent event) {
		event.getSleepingLocation().ifPresent(
				(pos) -> {
					if(event.getEntity().getLevel().getBlockState(pos).getBlock() instanceof Coffin) {
						final long time = event.getEntity().getLevel().getDayTime() % 24000L;
						if(time > 500 && time < 11500) event.setResult(Event.Result.ALLOW);
						else event.setResult(Event.Result.DENY);
					}
				}
		);
	}
}
