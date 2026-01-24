package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.FurnishContents;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class FurnishBlockTagsGenerator extends FabricTagProvider<Block> {
    public FurnishBlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, BuiltInRegistries.BLOCK.key(), registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(FurnishContents.PLACE_ON_STAIRS).forceAddTag(BlockTags.WOOL_CARPETS);
        getOrCreateTagBuilder(FurnishContents.PLACE_ON_TRAPDOOR).forceAddTag(BlockTags.WOOL_CARPETS);
    }
}
