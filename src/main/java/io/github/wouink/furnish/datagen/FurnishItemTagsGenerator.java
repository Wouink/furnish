package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.concurrent.CompletableFuture;

public class FurnishItemTagsGenerator extends FabricTagProvider.ItemTagProvider {

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

    public FurnishItemTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            String wood = set.woodType.name().toLowerCase();
            TagKey<Item> setTag = RegLib.registerTag(Registries.ITEM, wood + "_furniture");

            valueLookupBuilder(setTag).add(set.getAllItems());
            valueLookupBuilder(WOODEN_FURNITURE).forceAddTag(setTag);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                valueLookupBuilder(BEDSIDE_TABLES).add(set.bedsideTable.asItem());
                valueLookupBuilder(KITCHEN_CABINETS).add(set.kitchenCabinet.asItem());
            }

            valueLookupBuilder(BENCHES).add(set.bench.asItem());
            valueLookupBuilder(CABINETS).add(set.cabinet.asItem());
            valueLookupBuilder(CHAIRS).add(set.chair.asItem());
            valueLookupBuilder(CRATES).add(set.crate.asItem());
            valueLookupBuilder(LADDERS).add(set.ladder.asItem());
            valueLookupBuilder(LOG_BENCHES).add(set.logBench.asItem());
            valueLookupBuilder(PEDESTAL_TABLES).add(set.pedestalTable.asItem());
            valueLookupBuilder(SHELVES).add(set.shelf.asItem());
            valueLookupBuilder(SHUTTERS).add(set.shutter.asItem());
            valueLookupBuilder(SQUARE_TABLES).add(set.squareTable.asItem());
            valueLookupBuilder(STOOLS).add(set.stool.asItem());
            valueLookupBuilder(TABLES).add(set.table.asItem());
            valueLookupBuilder(WARDROBES).add(set.wardrobe.asItem());
        }

        valueLookupBuilder(AMPHORAE).add(FurnishContents.AMPHORA.asItem());
        valueLookupBuilder(PLATES).add(FurnishContents.PLATE.asItem(), FurnishContents.CHINESE_PLATE.asItem(), FurnishContents.ENGLISH_PLATE.asItem());

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            TagKey<Item> setTag = RegLib.registerTag(Registries.ITEM, color + "_furniture");
            valueLookupBuilder(setTag).add(set.getAllItems());

            valueLookupBuilder(AMPHORAE).add(set.amphora.asItem());
            valueLookupBuilder(AWNINGS).add(set.awning.asItem());
            valueLookupBuilder(SOFAS).add(set.sofa.asItem());
            valueLookupBuilder(SHOWCASES).add(set.showcase.asItem());
            valueLookupBuilder(PLATES).add(set.plate.asItem());
            valueLookupBuilder(PAPER_LAMPS).add(set.paperLamp.asItem());
            valueLookupBuilder(CURTAINS).add(set.curtain.asItem());
        }

        valueLookupBuilder(FurnishContents.CRATE_BLACKLIST_TAG).forceAddTag(CRATES);
        valueLookupBuilder(FurnishContents.CAN_CYCLE).add(Items.PAINTING);
        valueLookupBuilder(FurnishContents.MAIL).add(FurnishContents.LETTER).forceAddTag(CRATES);

        valueLookupBuilder(MAILBOXES).add(FurnishContents.METAL_MAILBOX.asItem());
        valueLookupBuilder(RECYCLE_BINS).add(FurnishContents.RECYCLE_BIN.asItem(), FurnishContents.TRASH_CAN.asItem());
        valueLookupBuilder(BUNTINGS).add(FurnishContents.GREEN_BUNTING.asItem(), FurnishContents.RED_BUNTING.asItem(), FurnishContents.YELLOW_BUNTING.asItem(), FurnishContents.SOUL_LANTERN_BUNTING.asItem(), FurnishContents.LANTERN_BUNTING.asItem());
        valueLookupBuilder(WARDROBES).add(FurnishContents.LOCKER.asItem());
        valueLookupBuilder(CABINETS).add(FurnishContents.SMALL_LOCKER.asItem());
        valueLookupBuilder(WOODEN_FURNITURE).add(FurnishContents.CHESS_BOARD.asItem(), FurnishContents.PICTURE_FRAME.asItem());
        valueLookupBuilder(WOODEN_FURNITURE).add(FurnishContents.DISK_RACK.asItem(), FurnishContents.FURNITURE_WORKBENCH.asItem());
        valueLookupBuilder(WOODEN_FURNITURE).forceAddTag(PAPER_LAMPS).forceAddTag(SOFAS).forceAddTag(AWNINGS);
    }
}
