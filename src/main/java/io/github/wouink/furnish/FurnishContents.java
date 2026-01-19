package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.blockentity.*;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.reglib.RegLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FurnishContents {

    public class Properties {
        public static final BooleanProperty RIGHT = BooleanProperty.create("right");
        public static final BooleanProperty LEFT = BooleanProperty.create("left");
        public static final BooleanProperty TOP = BooleanProperty.create("top");
    }

    public static final List<Block> smallFurniture = new ArrayList<>();
    public static final List<Block> largeFurniture = new ArrayList<>();
    public static final List<Block> amphorae = new ArrayList<>();
    public static final List<Block> crates = new ArrayList<>();
    public static final List<Block> shelves = new ArrayList<>();
    public static final List<Block> showcases = new ArrayList<>();
    public static final List<Block> plates = new ArrayList<>();
    public static final List<Block> shutters = new ArrayList<>();
    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> SMALL_FURNITURE_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> LARGE_FURNITURE_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> AMPHORA_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull CrateBlockEntity> CRATE_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull ShelfBlockEntity> SHELF_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull ShowcaseBlockEntity> SHOWCASE_BLOCK_ENTITY;
    public static BlockEntityType<@NotNull PlateBlockEntity> PLATE_BLOCK_ENTITY;

    public static SoundEvent CABINET_OPEN = RegLib.registerSound("block.furniture.open");
    public static SoundEvent CABINET_CLOSE = RegLib.registerSound("block.furniture.close");
    public static SoundEvent SPRUCE_CABINET_OPEN = RegLib.registerSound("block.spruce_furniture.open");
    public static SoundEvent SPRUCE_CABINET_CLOSE = RegLib.registerSound("block.spruce_furniture.close");
    public static SoundEvent DRAWER_OPEN = RegLib.registerSound("block.furniture_drawers.open");
    public static SoundEvent DRAWER_CLOSE = RegLib.registerSound("block.furniture_drawers.close");
    public static SoundEvent AMPHORA_OPEN = RegLib.registerSound("block.amphora.open");
    public static SoundEvent AMPHORA_CLOSE = RegLib.registerSound("block.amphora.close");

    public static final TagKey CRATE_BLACKLIST_TAG = RegLib.registerTag(Registries.ITEM, "crate_blacklist");
    public static final TagKey FOOD_TAG = RegLib.registerTag(Registries.ITEM, "food");

    public static EntityType<SeatEntity> SEAT_ENTITY = RegLib.registerEntityType(
            "seat",
            EntityType.Builder.of((entityType, level)
                    -> new SeatEntity(level), MobCategory.MISC).sized(0f, 0f)
    );

    public static CreativeModeTab FURNISH_TAB;

    // TODO bunting
    // TODO lantern bunting
    // TODO book pile
    // TODO carpet on stairs
    // TODO carpet on trapdoor
    // TODO chess board
    // TODO chimney conduit + chimney cap (or a simpler smoke emitting chimney block?)
    // TODO cobweb variant
    // TODO curtain
    // TODO disk rack
    // TODO dice?
    // TODO furniture workbench
    // TODO display??
    // TODO iron gate
    // TODO mailbox
    // TODO paper
    // TODO paper lamp => ok, add TOP/BOTTOM properties for support?
    // TODO picture frame
    // TODO recycle bin
    // TODO skull torch?
    // TODO snow on fence?

    public static void init() {
        for(WoodType woodType : WoodType.values().toList()) {
            String wood = woodType.name().toLowerCase();
            BlockBehaviour.Properties props = BlockBehaviour.Properties.of().sound(woodType.soundType());
            RegLib.registerBlock(wood + "_square_table", Block::new, props.noOcclusion(), true);
            RegLib.registerBlock(wood + "_pedestal_table", Block::new, props.noOcclusion(), true);

            if(!wood.equals("warped") && !wood.equals("crimson")) {
                smallFurniture.add(RegLib.registerBlock(wood + "_bedside_table", Drawer::new, props.noOcclusion(), true));
                smallFurniture.add(RegLib.registerBlock(wood + "_kitchen_cabinet", Drawer::new, props.noOcclusion(), true));
            }

            smallFurniture.add(RegLib.registerBlock(wood + "_cabinet", Cabinet::new, props.noOcclusion(), true));
            largeFurniture.add(RegLib.registerBlock(wood + "_wardrobe", Wardrobe::new, props.noOcclusion(), true));

            RegLib.registerBlock(wood + "_stool", Chair::new, props.noOcclusion(), true);
            Chair chair = (Chair) RegLib.registerBlock(wood + "_chair", Chair::new, props.noOcclusion(), true);
            if(wood.equals("acacia") || wood.equals("spruce"))
                chair.setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.TALL_SEAT));
            else if(wood.equals("dark_oak"))
                chair.setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.THRONE_SEAT));
            else
                chair.setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.SMALL_SEAT));
            RegLib.registerBlock(wood + "_bench", Bench::new, props.noOcclusion(), true);
            RegLib.registerBlock(wood + "_log_bench", LogBench::new, props.noOcclusion(), true);

            RegLib.registerBlock(wood + "_ladder", Ladder::new, props.noOcclusion(), true);
            RegLib.registerBlock(wood + "_table", Table::new, props.noOcclusion().forceSolidOn(), true);

            crates.add(RegLib.registerBlock(wood + "_crate", Crate::new, props, true));
            // TODO add shelf renderer
            shelves.add(RegLib.registerBlock(wood + "_shelf", Shelf::new, props.noOcclusion(), true));
            // TODO add drawers

            shutters.add(RegLib.registerBlock(wood + "_shutter", Shutter::new, props.noOcclusion(), true));
        }

        BlockBehaviour.Properties lockerProps = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops();
        largeFurniture.add(RegLib.registerBlock("locker", Wardrobe::new, lockerProps, true));
        smallFurniture.add(RegLib.registerBlock("small_locker", Cabinet::new, lockerProps, true));

        amphorae.add(RegLib.registerBlock("amphora", Amphora::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA), true));

        BlockBehaviour.Properties paperLampProps = BlockBehaviour.Properties.of().strength(.5f).sound(SoundType.SCAFFOLDING).noOcclusion().lightLevel(state -> 15);
        for(DyeColor dyeColor : DyeColor.values()) {
            String color = dyeColor.name().toLowerCase();
            Block terracotta = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_terracotta"));
            Block wool = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_wool"));
            Block carpet = BuiltInRegistries.BLOCK.get(ResourceLocation.withDefaultNamespace(color + "_carpet"));

            amphorae.add(RegLib.registerBlock(color + "_amphora", Amphora::new, BlockBehaviour.Properties.ofFullCopy(terracotta), true));
            RegLib.registerBlock(color + "_awning", Awning::new, BlockBehaviour.Properties.ofFullCopy(carpet).noOcclusion(), true);
            RegLib.registerBlock(color + "_sofa", Sofa::new, BlockBehaviour.Properties.ofFullCopy(wool).noOcclusion(), true);
            showcases.add(RegLib.registerBlock(color + "_showcase", Showcase::new, BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS), true));
            plates.add(RegLib.registerBlock(color + "_plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(terracotta), true));

            RegLib.registerBlock(color + "_paper_lamp", PaperLamp::new, paperLampProps, true);
        }

        SMALL_FURNITURE_BLOCK_ENTITY = RegLib.registerBlockEntity("furniture", SmallFurnitureBlockEntity::new, smallFurniture.toArray(new Block[]{}));
        LARGE_FURNITURE_BLOCK_ENTITY = RegLib.registerBlockEntity("large_furniture", LargeFurnitureBlockEntity::new, largeFurniture.toArray(new Block[]{}));
        AMPHORA_BLOCK_ENTITY = RegLib.registerBlockEntity("amphora", AmphoraBlockEntity::new, amphorae.toArray(new Block[]{}));
        CRATE_BLOCK_ENTITY = RegLib.registerBlockEntity("crate", CrateBlockEntity::new, crates.toArray(new Block[]{}));
        SHELF_BLOCK_ENTITY = RegLib.registerBlockEntity("shelf", ShelfBlockEntity::new, shelves.toArray(new Block[]{}));
        SHOWCASE_BLOCK_ENTITY = RegLib.registerBlockEntity("showcase", ShowcaseBlockEntity::new, showcases.toArray(new Block[]{}));
        PLATE_BLOCK_ENTITY = RegLib.registerBlockEntity("plate", PlateBlockEntity::new, plates.toArray(new Block[]{}));

        FURNISH_TAB = RegLib.registerCreativeTab("furnish", smallFurniture.get(0).asItem());
    }
}
