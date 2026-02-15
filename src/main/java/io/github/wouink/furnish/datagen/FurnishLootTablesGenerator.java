package io.github.wouink.furnish.datagen;

import io.github.wouink.furnish.ColoredSet;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.WoodenSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class FurnishLootTablesGenerator extends FabricBlockLootTableProvider {
    public FurnishLootTablesGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(FurnishContents.FURNITURE_WORKBENCH);
        dropSelf(FurnishContents.AMPHORA);
        dropSelf(FurnishContents.PLATE);
        dropSelf(FurnishContents.ENGLISH_PLATE);
        dropSelf(FurnishContents.CHINESE_PLATE);
        dropSelf(FurnishContents.RED_BUNTING);
        dropSelf(FurnishContents.YELLOW_BUNTING);
        dropSelf(FurnishContents.GREEN_BUNTING);
        dropOther(FurnishContents.LANTERN_BUNTING, Items.LANTERN);
        dropOther(FurnishContents.SOUL_LANTERN_BUNTING, Items.SOUL_LANTERN);
        dropSelf(FurnishContents.LOCKER);
        dropSelf(FurnishContents.SMALL_LOCKER);
        dropSelf(FurnishContents.METAL_MAILBOX);
        dropSelf(FurnishContents.RECYCLE_BIN);
        dropSelf(FurnishContents.TRASH_CAN);
        dropOther(FurnishContents.BOOK_PILE, Items.BOOK);
        dropSelf(FurnishContents.CHESS_BOARD);
        dropSelf(FurnishContents.PICTURE_FRAME); // TODO add the correct amount - see createCandleDrop
        dropSelf(FurnishContents.DISK_RACK);

        for(WoodenSet set : FurnishContents.WOODEN_SETS.values()) {
            for(Block b : set.getAllBlocks()) {
                if(b == null) continue;
                else if(b == set.crate) add(b, createShulkerBoxDrop(b));
                else dropSelf(b);
            }
        }

        for(ColoredSet set : FurnishContents.COLORED_SETS.values()) {
            dropSelf(set.amphora);
            dropSelf(set.awning);
            dropSelf(set.sofa);
            dropSelf(set.showcase);
            dropSelf(set.plate);
            dropSelf(set.paperLamp);
            dropSelf(set.curtain);
            dropOther(set.carpetOnTrapdoor, set.vanillaCarpet);
            dropOther(set.carpetOnStairs, set.vanillaCarpet);
        }
    }
}
