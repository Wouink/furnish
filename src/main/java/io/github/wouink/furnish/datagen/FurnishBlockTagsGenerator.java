package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.concurrent.CompletableFuture;

public class FurnishBlockTagsGenerator extends FabricTagProvider.BlockTagProvider {
    
    public static final TagKey<Block> WOODEN_FURNITURE = RegLib.registerTag(Registries.BLOCK, "wooden_furniture");
    public static final TagKey<Block> AMPHORAE = RegLib.registerTag(Registries.BLOCK, "amphorae");
    public static final TagKey<Block> AWNINGS = RegLib.registerTag(Registries.BLOCK, "awnings");
    public static final TagKey<Block> BEDSIDE_TABLES = RegLib.registerTag(Registries.BLOCK, "bedside_tables");
    public static final TagKey<Block> BENCHES = RegLib.registerTag(Registries.BLOCK, "benches");
    public static final TagKey<Block> BUNTINGS = RegLib.registerTag(Registries.BLOCK, "buntings");
    public static final TagKey<Block> CABINETS = RegLib.registerTag(Registries.BLOCK, "cabinets");
    public static final TagKey<Block> CHAIRS = RegLib.registerTag(Registries.BLOCK, "chairs");
    public static final TagKey<Block> CRATES = RegLib.registerTag(Registries.BLOCK, "crates");
    public static final TagKey<Block> CURTAINS = RegLib.registerTag(Registries.BLOCK, "curtains");
    public static final TagKey<Block> KITCHEN_CABINETS = RegLib.registerTag(Registries.BLOCK, "kitchen_cabinets");
    public static final TagKey<Block> LADDERS = RegLib.registerTag(Registries.BLOCK, "ladders");
    public static final TagKey<Block> LOG_BENCHES = RegLib.registerTag(Registries.BLOCK, "log_benches");
    public static final TagKey<Block> MAILBOXES = RegLib.registerTag(Registries.BLOCK, "mailboxes");
    public static final TagKey<Block> PAPER_LAMPS = RegLib.registerTag(Registries.BLOCK, "paper_lamps");
    public static final TagKey<Block> PEDESTAL_TABLES = RegLib.registerTag(Registries.BLOCK, "pedestal_tables");
    public static final TagKey<Block> PLATES = RegLib.registerTag(Registries.BLOCK, "plates");
    public static final TagKey<Block> RECYCLE_BINS = RegLib.registerTag(Registries.BLOCK, "recycle_bins");
    public static final TagKey<Block> SHELVES = RegLib.registerTag(Registries.BLOCK, "shelves");
    public static final TagKey<Block> SHOWCASES = RegLib.registerTag(Registries.BLOCK, "showcases");
    public static final TagKey<Block> SHUTTERS = RegLib.registerTag(Registries.BLOCK, "shutters");
    public static final TagKey<Block> SOFAS = RegLib.registerTag(Registries.BLOCK, "sofas");
    public static final TagKey<Block> SQUARE_TABLES = RegLib.registerTag(Registries.BLOCK, "square_tables");
    public static final TagKey<Block> STOOLS = RegLib.registerTag(Registries.BLOCK, "stools");
    public static final TagKey<Block> TABLES = RegLib.registerTag(Registries.BLOCK, "tables");
    public static final TagKey<Block> WARDROBES = RegLib.registerTag(Registries.BLOCK, "wardrobes");
    public static final TagKey<Block> CARPETS_ON_STAIRS = RegLib.registerTag(Registries.BLOCK, "carpets_on_stairs");
    public static final TagKey<Block> CARPETS_ON_TRAPDOORS = RegLib.registerTag(Registries.BLOCK, "carpets_on_trapdoors");

    public FurnishBlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(FurnishContents.PLACE_ON_STAIRS).forceAddTag(BlockTags.WOOL_CARPETS);
        valueLookupBuilder(FurnishContents.PLACE_ON_TRAPDOOR).forceAddTag(BlockTags.WOOL_CARPETS);

        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            String wood = set.woodType.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, wood + "_furniture");

            valueLookupBuilder(setTag).add(set.getAllBlocks());
            valueLookupBuilder(WOODEN_FURNITURE).forceAddTag(setTag);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                valueLookupBuilder(BEDSIDE_TABLES).add(set.bedsideTable);
                valueLookupBuilder(KITCHEN_CABINETS).add(set.kitchenCabinet);
            }

            valueLookupBuilder(BENCHES).add(set.bench);
            valueLookupBuilder(CABINETS).add(set.cabinet);
            valueLookupBuilder(CHAIRS).add(set.chair);
            valueLookupBuilder(CRATES).add(set.crate);
            valueLookupBuilder(LADDERS).add(set.ladder);
            valueLookupBuilder(LOG_BENCHES).add(set.logBench);
            valueLookupBuilder(PEDESTAL_TABLES).add(set.pedestalTable);
            valueLookupBuilder(SHELVES).add(set.shelf);
            valueLookupBuilder(SHUTTERS).add(set.shutter);
            valueLookupBuilder(SQUARE_TABLES).add(set.squareTable);
            valueLookupBuilder(STOOLS).add(set.stool);
            valueLookupBuilder(TABLES).add(set.table);
            valueLookupBuilder(WARDROBES).add(set.wardrobe);
        }

        valueLookupBuilder(AMPHORAE).add(FurnishContents.AMPHORA);
        valueLookupBuilder(PLATES).add(FurnishContents.PLATE, FurnishContents.CHINESE_PLATE, FurnishContents.ENGLISH_PLATE);

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, color + "_furniture");
            valueLookupBuilder(setTag).add(set.getAllBlocks());

            valueLookupBuilder(AMPHORAE).add(set.amphora);
            valueLookupBuilder(AWNINGS).add(set.awning);
            valueLookupBuilder(SOFAS).add(set.sofa);
            valueLookupBuilder(SHOWCASES).add(set.showcase);
            valueLookupBuilder(PLATES).add(set.plate);
            valueLookupBuilder(PAPER_LAMPS).add(set.paperLamp);
            valueLookupBuilder(CURTAINS).add(set.curtain);
            valueLookupBuilder(CARPETS_ON_STAIRS).add(set.carpetOnStairs);
            valueLookupBuilder(CARPETS_ON_TRAPDOORS).add(set.carpetOnTrapdoor);
        }

        valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE).forceAddTag(WOODEN_FURNITURE).setReplace(false);
        valueLookupBuilder(BlockTags.CLIMBABLE).forceAddTag(LADDERS).setReplace(false);
        valueLookupBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(FurnishContents.BOOK_PILE).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.METAL_MAILBOX).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.TRASH_CAN).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.LOCKER, FurnishContents.SMALL_LOCKER).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(PLATES).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(AMPHORAE).setReplace(false);
        valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(SHOWCASES).setReplace(false);
        valueLookupBuilder(BlockTags.WOOL).setReplace(false).forceAddTag(SOFAS).forceAddTag(AWNINGS).forceAddTag(CURTAINS);

        valueLookupBuilder(MAILBOXES).add(FurnishContents.METAL_MAILBOX);
        valueLookupBuilder(RECYCLE_BINS).add(FurnishContents.RECYCLE_BIN, FurnishContents.TRASH_CAN);
        valueLookupBuilder(BUNTINGS).add(FurnishContents.GREEN_BUNTING, FurnishContents.RED_BUNTING, FurnishContents.YELLOW_BUNTING, FurnishContents.SOUL_LANTERN_BUNTING, FurnishContents.LANTERN_BUNTING);
        valueLookupBuilder(WARDROBES).add(FurnishContents.LOCKER);
        valueLookupBuilder(CABINETS).add(FurnishContents.SMALL_LOCKER);
        valueLookupBuilder(WOODEN_FURNITURE).add(FurnishContents.CHESS_BOARD, FurnishContents.PICTURE_FRAME);
        valueLookupBuilder(WOODEN_FURNITURE).add(FurnishContents.DISK_RACK, FurnishContents.FURNITURE_WORKBENCH);
        valueLookupBuilder(WOODEN_FURNITURE).forceAddTag(PAPER_LAMPS).forceAddTag(SOFAS).forceAddTag(AWNINGS);

        valueLookupBuilder(FurnishContents.CAN_KNOCK_ON).forceAddTag(BlockTags.DOORS);
        valueLookupBuilder(FurnishContents.CAN_POP_BOOK).add(Blocks.LECTERN);
    }
}
