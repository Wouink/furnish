package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FurnishItemTagsGenerator extends FabricTagsProvider.ItemTagsProvider {

    public static final TagKey<Item> WOODEN_FURNITURE = RegLib.registerTag(Registries.ITEM, "wooden_furniture");
    public static final TagKey<Item> AMPHORAE = RegLib.registerTag(Registries.ITEM, "amphorae");
    public static final TagKey<Item> AWNINGS = RegLib.registerTag(Registries.ITEM, "awnings");
    public static final TagKey<Item> BEDSIDE_TABLES = RegLib.registerTag(Registries.ITEM, "bedside_tables");
    public static final TagKey<Item> BENCHES = RegLib.registerTag(Registries.ITEM, "benches");
    public static final TagKey<Item> BUNTINGS = RegLib.registerTag(Registries.ITEM, "buntings");
    public static final TagKey<Item> CABINETS = RegLib.registerTag(Registries.ITEM, "cabinets");
    public static final TagKey<Item> CHAIRS = RegLib.registerTag(Registries.ITEM, "chairs");
    public static final TagKey<Item> CRATES = RegLib.registerTag(Registries.ITEM, "crates");
    public static final TagKey<Item> CURTAINS = RegLib.registerTag(Registries.ITEM, "curtains");
    public static final TagKey<Item> KITCHEN_CABINETS = RegLib.registerTag(Registries.ITEM, "kitchen_cabinets");
    public static final TagKey<Item> LADDERS = RegLib.registerTag(Registries.ITEM, "ladders");
    public static final TagKey<Item> LOG_BENCHES = RegLib.registerTag(Registries.ITEM, "log_benches");
    public static final TagKey<Item> MAILBOXES = RegLib.registerTag(Registries.ITEM, "mailboxes");
    public static final TagKey<Item> PAPER_LAMPS = RegLib.registerTag(Registries.ITEM, "paper_lamps");
    public static final TagKey<Item> PEDESTAL_TABLES = RegLib.registerTag(Registries.ITEM, "pedestal_tables");
    public static final TagKey<Item> PLATES = RegLib.registerTag(Registries.ITEM, "plates");
    public static final TagKey<Item> RECYCLE_BINS = RegLib.registerTag(Registries.ITEM, "recycle_bins");
    public static final TagKey<Item> SHELVES = RegLib.registerTag(Registries.ITEM, "shelves");
    public static final TagKey<Item> SHOWCASES = RegLib.registerTag(Registries.ITEM, "showcases");
    public static final TagKey<Item> SHUTTERS = RegLib.registerTag(Registries.ITEM, "shutters");
    public static final TagKey<Item> SOFAS = RegLib.registerTag(Registries.ITEM, "sofas");
    public static final TagKey<Item> SQUARE_TABLES = RegLib.registerTag(Registries.ITEM, "square_tables");
    public static final TagKey<Item> STOOLS = RegLib.registerTag(Registries.ITEM, "stools");
    public static final TagKey<Item> TABLES = RegLib.registerTag(Registries.ITEM, "tables");
    public static final TagKey<Item> WARDROBES = RegLib.registerTag(Registries.ITEM, "wardrobes");

    public FurnishItemTagsGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            String wood = set.woodType.name().toLowerCase();
            TagKey<Item> setTag = RegLib.registerTag(Registries.ITEM, wood + "_furniture");

            builder(setTag).addAll(Arrays.stream(set.getAllItems()).map(item -> item.builtInRegistryHolder().key()).toList());
            builder(WOODEN_FURNITURE).forceAddTag(setTag);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                // Item.Properties#itemIdOrThrow
                builder(BEDSIDE_TABLES).add(set.bedsideTable.asItem().builtInRegistryHolder().key());
                builder(KITCHEN_CABINETS).add(set.kitchenCabinet.asItem().builtInRegistryHolder().key());
            }

            builder(BENCHES).add(set.bench.asItem().builtInRegistryHolder().key());
            builder(CABINETS).add(set.cabinet.asItem().builtInRegistryHolder().key());
            builder(CHAIRS).add(set.chair.asItem().builtInRegistryHolder().key());
            builder(CRATES).add(set.crate.asItem().builtInRegistryHolder().key());
            builder(LADDERS).add(set.ladder.asItem().builtInRegistryHolder().key());
            builder(LOG_BENCHES).add(set.logBench.asItem().builtInRegistryHolder().key());
            builder(PEDESTAL_TABLES).add(set.pedestalTable.asItem().builtInRegistryHolder().key());
            builder(SHELVES).add(set.shelf.asItem().builtInRegistryHolder().key());
            builder(SHUTTERS).add(set.shutter.asItem().builtInRegistryHolder().key());
            builder(SQUARE_TABLES).add(set.squareTable.asItem().builtInRegistryHolder().key());
            builder(STOOLS).add(set.stool.asItem().builtInRegistryHolder().key());
            builder(TABLES).add(set.table.asItem().builtInRegistryHolder().key());
            builder(WARDROBES).add(set.wardrobe.asItem().builtInRegistryHolder().key());
        }

        builder(AMPHORAE).add(FurnishContents.AMPHORA.asItem().builtInRegistryHolder().key());
        builder(PLATES).add(FurnishContents.PLATE.asItem().builtInRegistryHolder().key(), FurnishContents.CHINESE_PLATE.asItem().builtInRegistryHolder().key(), FurnishContents.ENGLISH_PLATE.asItem().builtInRegistryHolder().key());

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            TagKey<Item> setTag = RegLib.registerTag(Registries.ITEM, color + "_furniture");
            builder(setTag).addAll(Arrays.stream(set.getAllItems()).map(item -> item.builtInRegistryHolder().key()).toList());

            builder(AMPHORAE).add(set.amphora.asItem().builtInRegistryHolder().key());
            builder(AWNINGS).add(set.awning.asItem().builtInRegistryHolder().key());
            builder(SOFAS).add(set.sofa.asItem().builtInRegistryHolder().key());
            builder(SHOWCASES).add(set.showcase.asItem().builtInRegistryHolder().key());
            builder(PLATES).add(set.plate.asItem().builtInRegistryHolder().key());
            builder(PAPER_LAMPS).add(set.paperLamp.asItem().builtInRegistryHolder().key());
            builder(CURTAINS).add(set.curtain.asItem().builtInRegistryHolder().key());
        }

        builder(FurnishContents.CRATE_BLACKLIST_TAG).forceAddTag(CRATES);
        builder(FurnishContents.CAN_CYCLE).add(Items.PAINTING.builtInRegistryHolder().key());
        builder(FurnishContents.MAIL).add(FurnishContents.LETTER.builtInRegistryHolder().key()).forceAddTag(CRATES);

        builder(MAILBOXES).add(FurnishContents.METAL_MAILBOX.asItem().builtInRegistryHolder().key());
        builder(RECYCLE_BINS).add(FurnishContents.RECYCLE_BIN.asItem().builtInRegistryHolder().key(), FurnishContents.TRASH_CAN.asItem().builtInRegistryHolder().key());
        builder(BUNTINGS).add(FurnishContents.GREEN_BUNTING.asItem().builtInRegistryHolder().key(), FurnishContents.RED_BUNTING.asItem().builtInRegistryHolder().key(), FurnishContents.YELLOW_BUNTING.asItem().builtInRegistryHolder().key(), FurnishContents.SOUL_LANTERN_BUNTING.asItem().builtInRegistryHolder().key(), FurnishContents.LANTERN_BUNTING.asItem().builtInRegistryHolder().key());
        builder(WARDROBES).add(FurnishContents.LOCKER.asItem().builtInRegistryHolder().key());
        builder(CABINETS).add(FurnishContents.SMALL_LOCKER.asItem().builtInRegistryHolder().key());
        builder(WOODEN_FURNITURE).add(FurnishContents.CHESS_BOARD.asItem().builtInRegistryHolder().key(), FurnishContents.PICTURE_FRAME.asItem().builtInRegistryHolder().key());
        builder(WOODEN_FURNITURE).add(FurnishContents.DISK_RACK.asItem().builtInRegistryHolder().key(), FurnishContents.FURNITURE_WORKBENCH.asItem().builtInRegistryHolder().key());
        builder(WOODEN_FURNITURE).forceAddTag(PAPER_LAMPS).forceAddTag(SOFAS).forceAddTag(AWNINGS);
    }
}
