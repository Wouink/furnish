package io.github.wouink.furnish.event;

import io.github.wouink.furnish.Furnish;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AppendPlatesToLoot {

	private static final ResourceLocation ZOMBIE_LT = new ResourceLocation("minecraft", "entities/zombie");
	private static final ResourceLocation ZOMBIE_APPEND = new ResourceLocation(Furnish.MODID, "entities/rare_plates_from_zombie");

	@SubscribeEvent
	public static void onLootTablesLoad(LootTableLoadEvent event) {
		if(event.getName().equals(ZOMBIE_LT)) {
			event.getTable().addPool(LootPool.lootPool().add(TableLootEntry.lootTableReference(ZOMBIE_APPEND)).name("furnish_rare_plates").build());
		}
	}
}
