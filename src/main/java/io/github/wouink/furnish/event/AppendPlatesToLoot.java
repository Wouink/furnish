package io.github.wouink.furnish.event;

import io.github.wouink.furnish.Furnish;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AppendPlatesToLoot {

	private static final ResourceLocation ENDERMAN_LT = new ResourceLocation("minecraft", "entities/enderman");
	private static final ResourceLocation APPEND = new ResourceLocation(Furnish.MODID, "entities/rare_plates_from_enderman");

	@SubscribeEvent
	public static void onLootTablesLoad(LootTableLoadEvent event) {
		if(event.getName().equals(ENDERMAN_LT)) {
			// event.getTable().addPool(LootPool.lootPool().add(TableLootEntry.lootTableReference(APPEND)).name("furnish_rare_plates").build());
		}
	}
}
