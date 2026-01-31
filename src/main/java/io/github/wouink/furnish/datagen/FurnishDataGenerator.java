package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.FurnishContents;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FurnishDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(FurnishLootTablesGenerator::new);
		pack.addProvider(FurnishRecipesGenerator::new);
		pack.addProvider(FurnishItemTagsGenerator::new);
		pack.addProvider(FurnishBlockTagsGenerator::new);
	}
}
