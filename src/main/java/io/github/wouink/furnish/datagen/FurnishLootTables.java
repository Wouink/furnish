package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.FurnishContents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class FurnishLootTables extends FabricBlockLootTableProvider {
    public FurnishLootTables(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(FurnishContents.FURNITURE_WORKBENCH);
        dropSelf(FurnishContents.AMPHORA);
    }
}
