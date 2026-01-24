package io.github.wouink.furnish.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FurnishDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(FurnishLootTablesGenerator::new);
		pack.addProvider(FurnishRecipesGenerator::new);
		pack.addProvider(FurnishItemTagsGenerator::new);
	}
}
