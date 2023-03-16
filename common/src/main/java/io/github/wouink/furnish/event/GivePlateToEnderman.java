package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;

public class GivePlateToEnderman {

	public static EventResult onEndermanSpawn(Entity entity, Level level) {
		// todo if(event.getLevel().isClientSide() || ->>>event.loadedFromDisk()<<<-) return;
		if(level.isClientSide()) return EventResult.pass();
		if(!(entity instanceof EnderMan enderman)) return EventResult.pass();
		if(enderman.getCarriedBlock() == null && level.getRandom().nextInt(100) == 10) {
			enderman.setCarriedBlock(FurnishBlocks.Rare_Plates.get(level.getRandom().nextInt(FurnishBlocks.Rare_Plates.size())).get().defaultBlockState());
			return EventResult.interruptTrue();
		}
		return EventResult.interruptFalse();
	}
}
