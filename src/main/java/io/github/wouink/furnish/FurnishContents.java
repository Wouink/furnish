package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.ShapeHelper;
import io.github.wouink.furnish.blockentity.*;
import io.github.wouink.furnish.container.FurnitureWorkbenchMenu;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.event.PlaceCarpet;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.network.OpenGUIS2C;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import io.github.wouink.furnish.reglib.RegLib;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final List<Block> mailboxes = new ArrayList<>();

    public static SoundEvent CABINET_OPEN = RegLib.registerSound("block.furniture.open");
    public static SoundEvent CABINET_CLOSE = RegLib.registerSound("block.furniture.close");
    public static SoundEvent SPRUCE_CABINET_OPEN = RegLib.registerSound("block.furniture_spruce.open");
    public static SoundEvent SPRUCE_CABINET_CLOSE = RegLib.registerSound("block.furniture_spruce.close");
    public static SoundEvent DRAWER_OPEN = RegLib.registerSound("block.furniture_drawers.open");
    public static SoundEvent DRAWER_CLOSE = RegLib.registerSound("block.furniture_drawers.close");
    public static SoundEvent LOCKER_OPEN = RegLib.registerSound("block.furniture_locker.open");
    public static SoundEvent LOCKER_CLOSE = RegLib.registerSound("block.furniture_locker.close");
    public static SoundEvent AMPHORA_OPEN = RegLib.registerSound("block.amphora.open");
    public static SoundEvent AMPHORA_CLOSE = RegLib.registerSound("block.amphora.close");
    public static SoundEvent CURTAIN_TOGGLE = RegLib.registerSound("block.curtain.interact");
    public static SoundEvent MAILBOX_FLAG_TOGGLE = RegLib.registerSound("block.mailbox.update");
    public static SoundEvent NEW_MAIL = RegLib.registerSound("event.mail_received");
    public static SoundEvent ADD_ATTACHMENT = RegLib.registerSound("item.letter.add_attachment");
    public static SoundEvent REMOVE_ATTACHMENT = RegLib.registerSound("item.letter.remove_attachment");

    public static final TagKey CRATE_BLACKLIST_TAG = RegLib.registerTag(Registries.ITEM, "crate_blacklist");
    public static final TagKey FOOD_TAG = RegLib.registerTag(Registries.ITEM, "food");
    public static final TagKey PLACE_ON_STAIRS = RegLib.registerTag(Registries.BLOCK, "place_on_stairs");
    public static final TagKey PLACE_ON_TRAPDOOR = RegLib.registerTag(Registries.BLOCK, "place_on_trapdoor");
    public static final TagKey BYPASSES_MAIL = RegLib.registerTag(Registries.BLOCK, "bypasses_mail_tag");
    public static final TagKey NON_OP_CREATIVE_CAN_DESTROY = RegLib.registerTag(Registries.BLOCK, "non_op_creative_can_destroy");
    public static final TagKey MAIL = RegLib.registerTag(Registries.ITEM, "mail");

    public static EntityType<SeatEntity> SEAT_ENTITY = RegLib.registerEntityType(
            "seat",
            EntityType.Builder.of((entityType, level)
                    -> new SeatEntity(level), MobCategory.MISC).sized(0f, 0f)
    );

    public static final DataComponentType<String> LETTER_AUTHOR = RegLib.registerDataComponentType("letter_author");
    public static final DataComponentType<String> LETTER_TEXT = RegLib.registerDataComponentType("letter_text");

    public static MenuType<FurnitureWorkbenchMenu> WORKBENCH_MENU = RegLib.registerMenuType("furniture_workbench", FurnitureWorkbenchMenu::new);
    public static RecipeType<FurnitureRecipe> FURNITURE_RECIPE = RegLib.registerRecipeType("furniture_making");
    public static RecipeSerializer<FurnitureRecipe> FURNITURE_RECIPE_SERIALIZER = RegLib.registerRecipeSerializer("furniture_making", FurnitureRecipe.SERIALIZER);

    public static final Block FURNITURE_WORKBENCH = RegLib.registerBlock("furniture_workbench", FurnitureWorkbench::new, BlockBehaviour.Properties.of().strength(1.0f).sound(SoundType.WOOD).noOcclusion(), true);
    public static final CreativeModeTab FURNISH_TAB = RegLib.registerCreativeTab("furnish", FURNITURE_WORKBENCH.asItem());

    // TODO make a proper item texture for buntings
    // TODO add texture variation for each bunting
    private static final BlockBehaviour.Properties BUNTING_PROPS = BlockBehaviour.Properties.ofFullCopy(Blocks.TRIPWIRE).noOcclusion().noCollission();
    public static final Block LANTERN_BUNTING = RegLib.registerBlock("lantern_bunting", LanternBunting::new, BUNTING_PROPS, false);
    public static final Block SOUL_LANTERN_BUNTING = RegLib.registerBlock("soul_lantern_bunting", LanternBunting::new, BUNTING_PROPS, false);
    public static final Block RED_BUNTING = RegLib.registerBlock("red_bunting", Bunting::new, BUNTING_PROPS, true);
    public static final Block YELLOW_BUNTING = RegLib.registerBlock("yellow_bunting", Bunting::new, BUNTING_PROPS, true);
    public static final Block GREEN_BUNTING = RegLib.registerBlock("green_bunting", Bunting::new, BUNTING_PROPS, true);

    public static final Map<WoodType, WoodenSet> WOODEN_SETS = new HashMap<>();

    // this is where we register all the wooden furniture (cabinets, wardrobes, tables, chairs...)
    static {
        for(WoodType woodType : WoodType.values().toList()) WOODEN_SETS.put(woodType, new WoodenSet(woodType));
    }

    private static final BlockBehaviour.Properties LOCKER_PROPS = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops().noOcclusion();
    public static final Block LOCKER = RegLib.registerBlock("locker", Wardrobe::new, LOCKER_PROPS, true);
    public static final Block SMALL_LOCKER = RegLib.registerBlock("small_locker", Cabinet::new, LOCKER_PROPS, true);

    public static final Block METAL_MAILBOX = RegLib.registerBlock("metal_mailbox", Mailbox::new, LOCKER_PROPS, true);
    public static final Item LETTER = RegLib.registerItem("letter", Letter::new, new Item.Properties().stacksTo(1));

    static {
        ((Chair) WOODEN_SETS.get(WoodType.ACACIA).chair).setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.TALL_SEAT));
        ((Chair) WOODEN_SETS.get(WoodType.SPRUCE).chair).setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.TALL_SEAT));
        ((Chair) WOODEN_SETS.get(WoodType.DARK_OAK).chair).setShape(ShapeHelper.getMergedShapes(Chair.STOOL, Chair.THRONE_SEAT));;

        largeFurniture.add(LOCKER);
        smallFurniture.add(SMALL_LOCKER);

        mailboxes.add(METAL_MAILBOX);
    }

    public static final Block AMPHORA = RegLib.registerBlock("amphora", Amphora::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA), true);
    // TODO item properties: stacksTo 16 + rarity
    public static final Block PLATE = RegLib.registerBlock("plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA), true);
    public static final Block CHINESE_PLATE = RegLib.registerBlock("rare_chinese_plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA), true);
    public static final Block ENGLISH_PLATE = RegLib.registerBlock("rare_english_plate", Plate::new, BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA), true);

    public static final Map<DyeColor, ColoredSet> COLORED_SETS = new HashMap<>();

    // this is where we register all the colored furniture (amphorae, plates, showcases, sofas...)
    static {
        amphorae.add(AMPHORA);
        plates.add(PLATE);
        plates.add(CHINESE_PLATE);
        plates.add(ENGLISH_PLATE);

        for(DyeColor dyeColor : DyeColor.values()) COLORED_SETS.put(dyeColor, new ColoredSet(dyeColor));
    }

    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> SMALL_FURNITURE_BLOCK_ENTITY = RegLib.registerBlockEntity("furniture", SmallFurnitureBlockEntity::new, smallFurniture.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> LARGE_FURNITURE_BLOCK_ENTITY = RegLib.registerBlockEntity("large_furniture", LargeFurnitureBlockEntity::new, largeFurniture.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull AbstractFurnitureBlockEntity> AMPHORA_BLOCK_ENTITY = RegLib.registerBlockEntity("amphora", AmphoraBlockEntity::new, amphorae.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull CrateBlockEntity> CRATE_BLOCK_ENTITY = RegLib.registerBlockEntity("crate", CrateBlockEntity::new, crates.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull ShelfBlockEntity> SHELF_BLOCK_ENTITY = RegLib.registerBlockEntity("shelf", ShelfBlockEntity::new, shelves.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull ShowcaseBlockEntity> SHOWCASE_BLOCK_ENTITY = RegLib.registerBlockEntity("showcase", ShowcaseBlockEntity::new, showcases.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull PlateBlockEntity> PLATE_BLOCK_ENTITY = RegLib.registerBlockEntity("plate", PlateBlockEntity::new, plates.toArray(new Block[]{}));
    public static BlockEntityType<@NotNull MailboxBlockEntity> MAILBOX_BLOCK_ENTITY = RegLib.registerBlockEntity("mailbox", MailboxBlockEntity::new, mailboxes.toArray(new Block[]{}));

    // TODO book pile
    // TODO chess board
    // TODO chimney conduit + chimney cap (or a simpler smoke emitting chimney block?)
    // TODO cobweb variant
    // TODO disk rack
    // TODO dice?
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
        UseBlockCallback.EVENT.register(PlaceCarpet::rightClickOnStairs);
        RegLib.registerNetworkMessage(RegLib.MessageDirection.S2C, OpenGUIS2C.TYPE, OpenGUIS2C.CODEC);
    }
}
