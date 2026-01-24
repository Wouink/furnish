package io.github.wouink.furnish;

import io.github.wouink.furnish.datagen.FurnishLootTables;
import io.github.wouink.furnish.datagen.FurnishRecipes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FurnishDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(FurnishLootTables::new);
		pack.addProvider(FurnishRecipes::new);
	}
}
