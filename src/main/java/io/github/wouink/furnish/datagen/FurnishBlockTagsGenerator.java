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
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.concurrent.CompletableFuture;

public class FurnishBlockTagsGenerator extends FabricTagProvider<Block> {
    public FurnishBlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

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

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(FurnishContents.PLACE_ON_STAIRS).forceAddTag(BlockTags.WOOL_CARPETS);
        getOrCreateTagBuilder(FurnishContents.PLACE_ON_TRAPDOOR).forceAddTag(BlockTags.WOOL_CARPETS);

        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            String wood = set.woodType.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, wood + "_furniture");

            getOrCreateTagBuilder(setTag).add(set.getAllBlocks());
            getOrCreateTagBuilder(WOODEN_FURNITURE).forceAddTag(setTag);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                getOrCreateTagBuilder(BEDSIDE_TABLES).add(set.bedsideTable);
                getOrCreateTagBuilder(KITCHEN_CABINETS).add(set.kitchenCabinet);
            }

            getOrCreateTagBuilder(BENCHES).add(set.bench);
            getOrCreateTagBuilder(CABINETS).add(set.cabinet);
            getOrCreateTagBuilder(CHAIRS).add(set.chair);
            getOrCreateTagBuilder(CRATES).add(set.crate);
            getOrCreateTagBuilder(LADDERS).add(set.ladder);
            getOrCreateTagBuilder(LOG_BENCHES).add(set.logBench);
            getOrCreateTagBuilder(PEDESTAL_TABLES).add(set.pedestalTable);
            getOrCreateTagBuilder(SHELVES).add(set.shelf);
            getOrCreateTagBuilder(SHUTTERS).add(set.shutter);
            getOrCreateTagBuilder(SQUARE_TABLES).add(set.squareTable);
            getOrCreateTagBuilder(STOOLS).add(set.stool);
            getOrCreateTagBuilder(TABLES).add(set.table);
            getOrCreateTagBuilder(WARDROBES).add(set.wardrobe);
        }

        getOrCreateTagBuilder(AMPHORAE).add(FurnishContents.AMPHORA);
        getOrCreateTagBuilder(PLATES).add(FurnishContents.PLATE, FurnishContents.CHINESE_PLATE, FurnishContents.ENGLISH_PLATE);

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, color + "_furniture");
            getOrCreateTagBuilder(setTag).add(set.getAllBlocks());

            getOrCreateTagBuilder(AMPHORAE).add(set.amphora);
            getOrCreateTagBuilder(AWNINGS).add(set.awning);
            getOrCreateTagBuilder(SOFAS).add(set.sofa);
            getOrCreateTagBuilder(SHOWCASES).add(set.showcase);
            getOrCreateTagBuilder(PLATES).add(set.plate);
            getOrCreateTagBuilder(PAPER_LAMPS).add(set.paperLamp);
            getOrCreateTagBuilder(CURTAINS).add(set.curtain);
            getOrCreateTagBuilder(CARPETS_ON_STAIRS).add(set.carpetOnStairs);
            getOrCreateTagBuilder(CARPETS_ON_TRAPDOORS).add(set.carpetOnTrapdoor);
        }

        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE).forceAddTag(WOODEN_FURNITURE).setReplace(false);
        getOrCreateTagBuilder(BlockTags.CLIMBABLE).forceAddTag(LADDERS).setReplace(false);
        getOrCreateTagBuilder(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(FurnishContents.BOOK_PILE).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.METAL_MAILBOX).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.TRASH_CAN).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.LOCKER, FurnishContents.SMALL_LOCKER).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(PLATES).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(AMPHORAE).setReplace(false);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(SHOWCASES).setReplace(false);
        getOrCreateTagBuilder(BlockTags.WOOL).setReplace(false).forceAddTag(SOFAS).forceAddTag(AWNINGS).forceAddTag(CURTAINS);

        getOrCreateTagBuilder(MAILBOXES).add(FurnishContents.METAL_MAILBOX);
        getOrCreateTagBuilder(RECYCLE_BINS).add(FurnishContents.RECYCLE_BIN, FurnishContents.TRASH_CAN);
        getOrCreateTagBuilder(BUNTINGS).add(FurnishContents.GREEN_BUNTING, FurnishContents.RED_BUNTING, FurnishContents.YELLOW_BUNTING, FurnishContents.SOUL_LANTERN_BUNTING, FurnishContents.LANTERN_BUNTING);
        getOrCreateTagBuilder(WARDROBES).add(FurnishContents.LOCKER);
        getOrCreateTagBuilder(CABINETS).add(FurnishContents.SMALL_LOCKER);
        getOrCreateTagBuilder(WOODEN_FURNITURE).add(FurnishContents.CHESS_BOARD, FurnishContents.PICTURE_FRAME);
        getOrCreateTagBuilder(WOODEN_FURNITURE).add(FurnishContents.DISK_RACK, FurnishContents.FURNITURE_WORKBENCH);
        getOrCreateTagBuilder(WOODEN_FURNITURE).forceAddTag(PAPER_LAMPS).forceAddTag(SOFAS).forceAddTag(AWNINGS);
    }
}
