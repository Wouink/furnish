package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagAppender;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FurnishBlockTagsGenerator extends FabricTagsProvider.BlockTagsProvider {
    
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

    public FurnishBlockTagsGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(FurnishContents.PLACE_ON_STAIRS).forceAddTag(BlockTags.WOOL_CARPETS);
        builder(FurnishContents.PLACE_ON_TRAPDOOR).forceAddTag(BlockTags.WOOL_CARPETS);
        
        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            String wood = set.woodType.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, wood + "_furniture");

            builder(setTag).addAll(Arrays.stream(set.getAllBlocks()).map(block -> block.properties().blockId()).toList());
            builder(WOODEN_FURNITURE).forceAddTag(setTag);

            if(set.woodType != WoodType.CRIMSON && set.woodType != WoodType.WARPED) {
                builder(BEDSIDE_TABLES).add(set.bedsideTable.properties().blockId());
                builder(KITCHEN_CABINETS).add(set.kitchenCabinet.properties().blockId());
            }

            builder(BENCHES).add(set.bench.properties().blockId());
            builder(CABINETS).add(set.cabinet.properties().blockId());
            builder(CHAIRS).add(set.chair.properties().blockId());
            builder(CRATES).add(set.crate.properties().blockId());
            builder(LADDERS).add(set.ladder.properties().blockId());
            builder(LOG_BENCHES).add(set.logBench.properties().blockId());
            builder(PEDESTAL_TABLES).add(set.pedestalTable.properties().blockId());
            builder(SHELVES).add(set.shelf.properties().blockId());
            builder(SHUTTERS).add(set.shutter.properties().blockId());
            builder(SQUARE_TABLES).add(set.squareTable.properties().blockId());
            builder(STOOLS).add(set.stool.properties().blockId());
            builder(TABLES).add(set.table.properties().blockId());
            builder(WARDROBES).add(set.wardrobe.properties().blockId());
        }

        builder(AMPHORAE).add(FurnishContents.AMPHORA.properties().blockId());
        builder(PLATES).add(FurnishContents.PLATE.properties().blockId(), FurnishContents.CHINESE_PLATE.properties().blockId(), FurnishContents.ENGLISH_PLATE.properties().blockId());

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            String color = set.dyeColor.name().toLowerCase();
            TagKey<Block> setTag = RegLib.registerTag(Registries.BLOCK, color + "_furniture");
            builder(setTag).addAll(Arrays.stream(set.getAllBlocks()).map(block -> block.properties().blockId()).toList());

            builder(AMPHORAE).add(set.amphora.properties().blockId());
            builder(AWNINGS).add(set.awning.properties().blockId());
            builder(SOFAS).add(set.sofa.properties().blockId());
            builder(SHOWCASES).add(set.showcase.properties().blockId());
            builder(PLATES).add(set.plate.properties().blockId());
            builder(PAPER_LAMPS).add(set.paperLamp.properties().blockId());
            builder(CURTAINS).add(set.curtain.properties().blockId());
            builder(CARPETS_ON_STAIRS).add(set.carpetOnStairs.properties().blockId());
            builder(CARPETS_ON_TRAPDOORS).add(set.carpetOnTrapdoor.properties().blockId());
        }

        builder(BlockTags.MINEABLE_WITH_AXE).forceAddTag(WOODEN_FURNITURE).setReplace(false);
        builder(BlockTags.CLIMBABLE).forceAddTag(LADDERS).setReplace(false);
        builder(BlockTags.ENCHANTMENT_POWER_PROVIDER).add(FurnishContents.BOOK_PILE.properties().blockId()).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.METAL_MAILBOX.properties().blockId()).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.TRASH_CAN.properties().blockId()).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).add(FurnishContents.LOCKER.properties().blockId(), FurnishContents.SMALL_LOCKER.properties().blockId()).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(PLATES).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(AMPHORAE).setReplace(false);
        builder(BlockTags.MINEABLE_WITH_PICKAXE).forceAddTag(SHOWCASES).setReplace(false);
        builder(BlockTags.WOOL).setReplace(false).forceAddTag(SOFAS).forceAddTag(AWNINGS).forceAddTag(CURTAINS);

        builder(MAILBOXES).add(FurnishContents.METAL_MAILBOX.properties().blockId());
        builder(RECYCLE_BINS).add(FurnishContents.RECYCLE_BIN.properties().blockId(), FurnishContents.TRASH_CAN.properties().blockId());
        builder(BUNTINGS).add(FurnishContents.GREEN_BUNTING.properties().blockId(), FurnishContents.RED_BUNTING.properties().blockId(), FurnishContents.YELLOW_BUNTING.properties().blockId(), FurnishContents.SOUL_LANTERN_BUNTING.properties().blockId(), FurnishContents.LANTERN_BUNTING.properties().blockId());
        builder(WARDROBES).add(FurnishContents.LOCKER.properties().blockId());
        builder(CABINETS).add(FurnishContents.SMALL_LOCKER.properties().blockId());
        builder(WOODEN_FURNITURE).add(FurnishContents.CHESS_BOARD.properties().blockId(), FurnishContents.PICTURE_FRAME.properties().blockId());
        builder(WOODEN_FURNITURE).add(FurnishContents.DISK_RACK.properties().blockId(), FurnishContents.FURNITURE_WORKBENCH.properties().blockId());
        builder(WOODEN_FURNITURE).forceAddTag(PAPER_LAMPS).forceAddTag(SOFAS).forceAddTag(AWNINGS);

        builder(FurnishContents.CAN_KNOCK_ON).forceAddTag(BlockTags.DOORS);
        builder(FurnishContents.CAN_POP_BOOK).add(Blocks.LECTERN.properties().blockId());
    }
}
