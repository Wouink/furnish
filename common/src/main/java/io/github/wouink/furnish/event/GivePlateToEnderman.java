package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;

public class GivePlateToEnderman {

	public static EventResult onEndermanSpawn(Entity entity, Level level) {
		if(!level.isClientSide()) {
			if(entity instanceof EnderMan enderman) {
				Furnish.debug("Enderman is " + enderman.tickCount + " ticks old");
				// ^ it's ok, the event is only fired when an entity is spawned (not when loading existing entities at chunk load)
				if(enderman.getCarriedBlock() == null && level.getRandom().nextInt(100) == 10) {
					enderman.setCarriedBlock(FurnishBlocks.Rare_Plates.get(level.getRandom().nextInt(FurnishBlocks.Rare_Plates.size())).get().defaultBlockState());
					Furnish.debug("Giving plate to an Enderman");
					// todo allow enderman to despawn?
				}
			}
		}
		return EventResult.pass();
	}
}
