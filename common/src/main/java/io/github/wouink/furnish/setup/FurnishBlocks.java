package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class FurnishBlocks {
	
	// Registries
	
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Furnish.MODID, Registry.BLOCK_REGISTRY);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Furnish.MODID, Registry.ITEM_REGISTRY);
	
	// Block related registering methods

	public static RegistrySupplier<Block> registerBlock(String name, Supplier<? extends Block> sup) {
		return BLOCKS.register(name, sup);
	}

	public static RegistrySupplier<Block> registerBlockWithItem(String name, Supplier<? extends Block> sup, Item.Properties itemProps) {
		RegistrySupplier<Block> registeredBlock = BLOCKS.register(name, sup);
		ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), itemProps.tab(Furnish.CREATIVE_TAB)));
		return registeredBlock;
	}

	public static RegistrySupplier<Block> registerBlockWithItem(String name, Supplier<? extends Block> sup) {
		return registerBlockWithItem(name, sup, new Item.Properties());
	}

	// Registering items

	public static final RegistrySupplier<Item> Letter = ITEMS.register("letter", () -> new Letter(new Item.Properties().tab(Furnish.CREATIVE_TAB).stacksTo(1)));

	// Various lists of blocks

	public static ArrayList<Block> Furniture_3x9 = new ArrayList<>();
	public static ArrayList<Block> Furniture_6x9 = new ArrayList<>();
	public static ArrayList<Block> Shelves = new ArrayList<>();
	public static ArrayList<Block> Recycle_Bins = new ArrayList<>();
	public static ArrayList<Block> Flower_Pots = new ArrayList<>();
	public static HashMap<String, RegistrySupplier<Block>> Carpets_On_Stairs = new HashMap<>(16);
	public static HashMap<String, RegistrySupplier<Block>> Carpets_On_Trapdoors = new HashMap<>(16);
	public static ArrayList<RegistrySupplier<Block>> Amphorae = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Sofas = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Awnings = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Curtains = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Plates = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Rare_Plates = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Showcases = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Paper_Lamps = new ArrayList<>();

	// Custom BlockStateProperties

	public static class CustomProperties {
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
		public static final BooleanProperty LEFT = BooleanProperty.create("left");
		public static final IntegerProperty COUNT_3 = IntegerProperty.create("count", 1, 3);
	}

	// All blocks

	public static final RegistrySupplier<Block> Furniture_Workbench = registerBlockWithItem("furniture_workbench", FurnitureWorkbench::new);
	public static final RegistrySupplier<Block> Book_Pile = registerBlockWithItem("book_pile", () -> new BookPile(BlockBehaviour.Properties.of(Material.WOOL).sound(SoundType.WOOL).strength(0.2f)));

	public static final RegistrySupplier<Block> Oak_Table = registerBlockWithItem("oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Square_Table = registerBlockWithItem("oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Pedestal_Table = registerBlockWithItem("oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), false));
	public static final RegistrySupplier<Block> Oak_Bedside_Table = registerBlockWithItem("oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Oak_Kitchen_Cabinet = registerBlockWithItem("oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Oak_Cabinet = registerBlockWithItem("oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Oak_Wardrobe = registerBlockWithItem("oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Oak_Stool = registerBlockWithItem("oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Oak_Chair = registerBlockWithItem("oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Oak_Shutter = registerBlockWithItem("oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final RegistrySupplier<Block> Oak_Crate = registerBlockWithItem("oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Oak_Shelf = registerBlockWithItem("oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Bench = registerBlockWithItem("oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Log_Bench = registerBlockWithItem("oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Ladder = registerBlockWithItem("oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Birch_Table = registerBlockWithItem("birch_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Square_Table = registerBlockWithItem("birch_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Pedestal_Table = registerBlockWithItem("birch_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), false));
	public static final RegistrySupplier<Block> Birch_Bedside_Table = registerBlockWithItem("birch_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Birch_Kitchen_Cabinet = registerBlockWithItem("birch_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Birch_Cabinet = registerBlockWithItem("birch_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Birch_Wardrobe = registerBlockWithItem("birch_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Birch_Stool = registerBlockWithItem("birch_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Birch_Chair = registerBlockWithItem("birch_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Birch_Shutter = registerBlockWithItem("birch_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final RegistrySupplier<Block> Birch_Crate = registerBlockWithItem("birch_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Birch_Shelf = registerBlockWithItem("birch_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Bench = registerBlockWithItem("birch_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Log_Bench = registerBlockWithItem("birch_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Ladder = registerBlockWithItem("birch_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Acacia_Table = registerBlockWithItem("acacia_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Square_Table = registerBlockWithItem("acacia_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Pedestal_Table = registerBlockWithItem("acacia_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), false));
	public static final RegistrySupplier<Block> Acacia_Bedside_Table = registerBlockWithItem("acacia_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Acacia_Kitchen_Cabinet = registerBlockWithItem("acacia_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Acacia_Cabinet = registerBlockWithItem("acacia_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Acacia_Wardrobe = registerBlockWithItem("acacia_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Acacia_Stool = registerBlockWithItem("acacia_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Acacia_Chair = registerBlockWithItem("acacia_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistrySupplier<Block> Acacia_Shutter = registerBlockWithItem("acacia_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final RegistrySupplier<Block> Acacia_Crate = registerBlockWithItem("acacia_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Acacia_Shelf = registerBlockWithItem("acacia_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Bench = registerBlockWithItem("acacia_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Log_Bench = registerBlockWithItem("acacia_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Ladder = registerBlockWithItem("acacia_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Jungle_Table = registerBlockWithItem("jungle_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Square_Table = registerBlockWithItem("jungle_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Pedestal_Table = registerBlockWithItem("jungle_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), false));
	public static final RegistrySupplier<Block> Jungle_Bedside_Table = registerBlockWithItem("jungle_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Jungle_Kitchen_Cabinet = registerBlockWithItem("jungle_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Jungle_Cabinet = registerBlockWithItem("jungle_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Jungle_Wardrobe = registerBlockWithItem("jungle_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Jungle_Stool = registerBlockWithItem("jungle_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Jungle_Chair = registerBlockWithItem("jungle_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Jungle_Shutter = registerBlockWithItem("jungle_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final RegistrySupplier<Block> Jungle_Crate = registerBlockWithItem("jungle_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Jungle_Shelf = registerBlockWithItem("jungle_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Bench = registerBlockWithItem("jungle_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Log_Bench = registerBlockWithItem("jungle_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Ladder = registerBlockWithItem("jungle_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Spruce_Table = registerBlockWithItem("spruce_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Square_Table = registerBlockWithItem("spruce_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Pedestal_Table = registerBlockWithItem("spruce_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), false));
	public static final RegistrySupplier<Block> Spruce_Bedside_Table = registerBlockWithItem("spruce_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Spruce_Kitchen_Cabinet = registerBlockWithItem("spruce_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Spruce_Cabinet = registerBlockWithItem("spruce_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishRegistries.Spruce_Cabinet_Open_Sound, FurnishRegistries.Spruce_Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Spruce_Wardrobe = registerBlockWithItem("spruce_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishRegistries.Spruce_Cabinet_Open_Sound, FurnishRegistries.Spruce_Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Spruce_Stool = registerBlockWithItem("spruce_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Spruce_Chair = registerBlockWithItem("spruce_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistrySupplier<Block> Spruce_Shutter = registerBlockWithItem("spruce_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final RegistrySupplier<Block> Spruce_Crate = registerBlockWithItem("spruce_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Spruce_Shelf = registerBlockWithItem("spruce_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Bench = registerBlockWithItem("spruce_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Log_Bench = registerBlockWithItem("spruce_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Ladder = registerBlockWithItem("spruce_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Dark_Oak_Table = registerBlockWithItem("dark_oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Square_Table = registerBlockWithItem("dark_oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Pedestal_Table = registerBlockWithItem("dark_oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), false));
	public static final RegistrySupplier<Block> Dark_Oak_Bedside_Table = registerBlockWithItem("dark_oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Dark_Oak_Kitchen_Cabinet = registerBlockWithItem("dark_oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Dark_Oak_Cabinet = registerBlockWithItem("dark_oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishRegistries.Spruce_Cabinet_Open_Sound, FurnishRegistries.Spruce_Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Dark_Oak_Wardrobe = registerBlockWithItem("dark_oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishRegistries.Spruce_Cabinet_Open_Sound, FurnishRegistries.Spruce_Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Dark_Oak_Stool = registerBlockWithItem("dark_oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Dark_Oak_Chair = registerBlockWithItem("dark_oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final RegistrySupplier<Block> Dark_Oak_Shutter = registerBlockWithItem("dark_oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final RegistrySupplier<Block> Dark_Oak_Crate = registerBlockWithItem("dark_oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Dark_Oak_Shelf = registerBlockWithItem("dark_oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Bench = registerBlockWithItem("dark_oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Log_Bench = registerBlockWithItem("dark_oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Ladder = registerBlockWithItem("dark_oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Crimson_Drawers = registerBlockWithItem("crimson_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Crimson_Cabinet = registerBlockWithItem("crimson_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Crimson_Wardrobe = registerBlockWithItem("crimson_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Crimson_Stool = registerBlockWithItem("crimson_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Crimson_Chair = registerBlockWithItem("crimson_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Crimson_Table = registerBlockWithItem("crimson_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Square_Table = registerBlockWithItem("crimson_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Pedestal_Table = registerBlockWithItem("crimson_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Shutter = registerBlockWithItem("crimson_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR)));
	public static final RegistrySupplier<Block> Crimson_Crate = registerBlockWithItem("crimson_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Crimson_Shelf = registerBlockWithItem("crimson_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Bench = registerBlockWithItem("crimson_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Log_Bench = registerBlockWithItem("crimson_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Ladder = registerBlockWithItem("crimson_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Warped_Drawers = registerBlockWithItem("warped_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishRegistries.Drawers_Open_Sound, FurnishRegistries.Drawers_Close_Sound));
	public static final RegistrySupplier<Block> Warped_Cabinet = registerBlockWithItem("warped_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Warped_Wardrobe = registerBlockWithItem("warped_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishRegistries.Cabinet_Open_Sound, FurnishRegistries.Cabinet_Close_Sound));
	public static final RegistrySupplier<Block> Warped_Stool = registerBlockWithItem("warped_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Warped_Chair = registerBlockWithItem("warped_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Warped_Table = registerBlockWithItem("warped_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Square_Table = registerBlockWithItem("warped_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Pedestal_Table = registerBlockWithItem("warped_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Shutter = registerBlockWithItem("warped_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.WARPED_TRAPDOOR)));
	public static final RegistrySupplier<Block> Warped_Crate = registerBlockWithItem("warped_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistrySupplier<Block> Warped_Shelf = registerBlockWithItem("warped_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Bench = registerBlockWithItem("warped_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Log_Bench = registerBlockWithItem("warped_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Ladder = registerBlockWithItem("warped_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Small_Locker = registerBlockWithItem("small_locker", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishRegistries.Locker_Open_Sound, FurnishRegistries.Locker_Close_Sound));
	public static final RegistrySupplier<Block> Locker = registerBlockWithItem("locker", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishRegistries.Locker_Open_Sound, FurnishRegistries.Locker_Close_Sound));

	public static final RegistrySupplier<Block> Red_Bunting = registerBlockWithItem("red_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Yellow_Bunting = registerBlockWithItem("yellow_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Green_Bunting = registerBlockWithItem("green_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Lantern_Bunting = registerBlock("lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 15)));
	public static final RegistrySupplier<Block> Soul_Lantern_Bunting = registerBlock("soul_lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 10)));

	public static final RegistrySupplier<Block> Metal_Mailbox = registerBlockWithItem("metal_mailbox", () -> new Mailbox(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL)));

	public static final RegistrySupplier<Block> Brick_Chimney_Conduit = registerBlockWithItem("brick_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Blackstone_Chimney_Conduit = registerBlockWithItem("blackstone_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Stone_Bricks_Chimney_Conduit = registerBlockWithItem("stone_bricks_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Chimney_Cap = registerBlockWithItem("chimney_cap", () -> new ChimneyCap(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final RegistrySupplier<Block> Disk_Rack = registerBlockWithItem("disk_rack", () -> new DiskRack(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	public static final RegistrySupplier<Block> Recycle_Bin = registerBlockWithItem("recycle_bin", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).strength(0.5f), FurnishRegistries.Recycle_Bin_Empty_Sound));
	public static final RegistrySupplier<Block> Trash_Can = registerBlockWithItem("trash_can", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.0f), FurnishRegistries.Trash_Can_Empty_Sound));

	// Halloween Update
	public static final RegistrySupplier<Block> Oak_Coffin = registerBlockWithItem("oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Coffin = registerBlockWithItem("birch_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Coffin = registerBlockWithItem("spruce_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Coffin = registerBlockWithItem("acacia_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Coffin = registerBlockWithItem("dark_oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Coffin = registerBlockWithItem("jungle_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Gravestone = registerBlockWithItem("gravestone", () -> new VerticalSlab(BlockBehaviour.Properties.copy(Blocks.ANDESITE)));
	public static final RegistrySupplier<Block> Iron_Bars_Top = registerBlockWithItem("iron_bars_top", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistrySupplier<Block> Iron_Gate = registerBlockWithItem("iron_gate", () -> new IronGate(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistrySupplier<Block> Skull_Torch = registerBlockWithItem("skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 10), ParticleTypes.FLAME));
	public static final RegistrySupplier<Block> Wither_Skull_Torch = registerBlockWithItem("wither_skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 7), ParticleTypes.SOUL_FIRE_FLAME));
	public static final RegistrySupplier<Block> Cobweb = registerBlockWithItem("cobweb", () -> new Cobweb(BlockBehaviour.Properties.of(Material.WEB).strength(0.0f)));

	public static final RegistrySupplier<Block> Picture_Frame = registerBlockWithItem("picture_frame", () -> new PictureFrame(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(SoundType.SCAFFOLDING).noCollission().noOcclusion()));
	// public static final RegistrySupplier<Block> Flower_Pot = registerBlockWithItem("flower_pot", () -> new FlowerPot(BlockBehaviour.Properties.of(Material.CLAY).strength(1.0f).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops(), 1));

	// Winter
	public static final RegistrySupplier<Block> Snow_On_Fence = registerBlock("snow_on_fence", () -> new SnowOnFence(BlockBehaviour.Properties.copy(Blocks.SNOW)));
	public static final RegistrySupplier<Block> Snow_On_Stairs = registerBlock("snow_on_stairs", () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(Blocks.SNOW), Blocks.SNOW));

	public static final RegistrySupplier<Block> Chess_Board = registerBlockWithItem("chess_board", () -> new ChessBoard(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(.5f)));

	// Office furniture
	public static final RegistrySupplier<Block> Paper_Sheet_Empty = registerBlockWithItem("paper_sheet_empty", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Sheet_Filled = registerBlockWithItem("paper_sheet_filled", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Sheet_Empty_Ink = registerBlockWithItem("paper_sheet_empty_ink", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Sheet_Filled_Ink = registerBlockWithItem("paper_sheet_filled_ink", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Sheet_Side = registerBlockWithItem("paper_sheet_side", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Sheet_Side_Filled = registerBlockWithItem("paper_sheet_side_filled", () -> new Paper(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Poster = registerBlockWithItem("paper_poster", () -> new Display(BlockBehaviour.Properties.of(Material.WOOD).noCollission().noOcclusion().instabreak().sound(FurnishRegistries.Paper_Sound_Type)));
	public static final RegistrySupplier<Block> Paper_Postit = registerBlockWithItem("paper_postit", () -> new Display(BlockBehaviour.Properties.of(Material.WOOD).noCollission().noOcclusion().instabreak().sound(FurnishRegistries.Paper_Sound_Type)));

	//

	public static final RegistrySupplier<Block> Telescope = registerBlockWithItem("telescope", () -> new SimpleFurniture(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).noOcclusion().sound(SoundType.WOOD)));
	public static final RegistrySupplier<Block> Dice = registerBlockWithItem("dice", () -> new Dice(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK)));
	public static final RegistrySupplier<Block> Asphalt = registerBlockWithItem("asphalt", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).speedFactor(1.2f)));

	static {
		Amphorae.add(registerBlockWithItem("amphora", () -> new Amphora(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		Plates.add(registerBlockWithItem("plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));

		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, registerBlock(String.format("%s_carpet_on_stairs", color), () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, registerBlock(String.format("%s_carpet_on_trapdoor", color), () -> new CarpetOnTrapdoor(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings.add(registerBlockWithItem(String.format("%s_awning", color), () -> new Awning(BlockBehaviour.Properties.copy(coloredCarpet))));
			Curtains.add(registerBlockWithItem(String.format("%s_curtain", color), () -> new Curtain(BlockBehaviour.Properties.copy(coloredCarpet))));

			Block coloredWool = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas.add(registerBlockWithItem(String.format("%s_sofa", color), () -> new Sofa(BlockBehaviour.Properties.copy(coloredWool))));
			Showcases.add(registerBlockWithItem(String.format("%s_showcase", color), () -> new Showcase(BlockBehaviour.Properties.copy(Blocks.GLASS))));

			Block coloredTerracotta = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae.add(registerBlockWithItem(String.format("%s_amphora", color), () -> new Amphora(BlockBehaviour.Properties.copy(coloredTerracotta))));
			Plates.add(registerBlockWithItem(String.format("%s_plate", color), () -> new Plate(BlockBehaviour.Properties.copy(coloredTerracotta)), new Item.Properties().stacksTo(16)));

			Paper_Lamps.add(registerBlockWithItem(String.format("%s_paper_lamp", color), PaperLamp::new));
		}

		Rare_Plates.add(registerBlockWithItem("rare_english_plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)), new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
		Rare_Plates.add(registerBlockWithItem("rare_chinese_plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)), new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));

		Plates.addAll(Rare_Plates);
	}
}
