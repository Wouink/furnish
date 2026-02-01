package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.reglib.RegLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.Arrays;

public class WoodenSet {
    public Block squareTable, pedestalTable, table, bedsideTable,
            kitchenCabinet, cabinet, wardrobe, stool, chair, bench,
            logBench, ladder, crate, shelf, shutter;
    public WoodType woodType;

    public WoodenSet(WoodType woodType) {
        this.woodType = woodType;

        String wood = woodType.name().toLowerCase();
        Block planks = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(wood + "_planks"));
        BlockBehaviour.Properties props = BlockBehaviour.Properties.ofFullCopy(planks);

        squareTable = RegLib.registerBlock(wood + "_square_table", Block::new, props.noOcclusion(), true);
        pedestalTable = RegLib.registerBlock(wood + "_pedestal_table", Block::new, props.noOcclusion(), true);

        if(woodType != WoodType.WARPED && woodType != WoodType.CRIMSON) {
            bedsideTable = RegLib.registerBlock(wood + "_bedside_table", Drawer::new, props.noOcclusion(), true);
            kitchenCabinet = RegLib.registerBlock(wood + "_kitchen_cabinet", Drawer::new, props.noOcclusion(), true);
            FurnishContents.smallFurniture.add(bedsideTable);
            FurnishContents.smallFurniture.add(kitchenCabinet);
        }

        cabinet = RegLib.registerBlock(wood + "_cabinet", Cabinet::new, props.noOcclusion(), true);
        FurnishContents.smallFurniture.add(cabinet);

        wardrobe = RegLib.registerBlock(wood + "_wardrobe", Wardrobe::new, props.noOcclusion(), true);
        FurnishContents.largeFurniture.add(wardrobe);

        stool = RegLib.registerBlock(wood + "_stool", Chair::new, props.noOcclusion().strength(.7f), true);
        chair = RegLib.registerBlock(wood + "_chair", Chair::new, props.noOcclusion().strength(.7f), true);
        ((Chair) chair).setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.SMALL_SEAT));

        bench = RegLib.registerBlock(wood + "_bench", Bench::new, props.noOcclusion().strength(.7f), true);
        logBench = RegLib.registerBlock(wood + "_log_bench", LogBench::new, props.noOcclusion(), true);

        ladder = RegLib.registerBlock(wood + "_ladder", Ladder::new, props.noOcclusion(), true);
        table = RegLib.registerBlock(wood + "_table", Table::new, props.noOcclusion().forceSolidOn(), true);

        crate = RegLib.registerBlock(wood + "_crate", Crate::new, props, true);
        FurnishContents.crates.add(crate);

        shelf = RegLib.registerBlock(wood + "_shelf", Shelf::new, props.noOcclusion().strength(.7f), true);
        FurnishContents.shelves.add(shelf);

        shutter = RegLib.registerBlock(wood + "_shutter", Shutter::new, props.noOcclusion(), true);
        FurnishContents.shutters.add(shutter);

        // TODO add drawers
    }

    public Block[] getAllBlocks() {
        if(woodType == WoodType.CRIMSON || woodType == WoodType.WARPED)
            return new Block[]{squareTable, pedestalTable, table, cabinet,
                    wardrobe, stool, chair, bench, logBench, ladder, crate, shelf, shutter};
        else
            return new Block[]{squareTable, pedestalTable, table, bedsideTable,
                    kitchenCabinet, cabinet, wardrobe, stool, chair, bench,
                    logBench, ladder, crate, shelf, shutter};
    }

    public Item[] getAllItems() {
        return Arrays.stream(getAllBlocks())
                .filter(block -> block.asItem() != Items.AIR)
                .map(block -> block.asItem()).toList().toArray(new Item[]{});
    }
}
