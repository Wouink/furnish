package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FurnishItemTagsGenerator extends FabricTagProvider<Item> {
    public FurnishItemTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, BuiltInRegistries.ITEM.key(), registriesFuture);
    }

    public static final TagKey<Item> CRATES = RegLib.registerTag(BuiltInRegistries.ITEM.key(), "crates");

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        List<Item> crates = new ArrayList<>();
        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) crates.add(set.crate.asItem());

        getOrCreateTagBuilder(CRATES).add(crates.toArray(new Item[]{}));
        getOrCreateTagBuilder(FurnishContents.CRATE_BLACKLIST_TAG).forceAddTag(CRATES);
        getOrCreateTagBuilder(FurnishContents.CAN_CYCLE).add(Items.PAINTING);
        getOrCreateTagBuilder(FurnishContents.MAIL).add(FurnishContents.LETTER).forceAddTag(CRATES);
    }
}
