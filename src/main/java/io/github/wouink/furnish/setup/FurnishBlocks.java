package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class FurnishBlocks {
	public static final DeferredRegister<Block> Registry = DeferredRegister.create(ForgeRegistries.BLOCKS, Furnish.MODID);

	public static ArrayList<Block> Furniture_3x9 = new ArrayList<>();
	public static ArrayList<Block> Furniture_6x9 = new ArrayList<>();
	public static ArrayList<Block> Shelves = new ArrayList<>();
	public static ArrayList<Block> Recycle_Bins = new ArrayList<>();
	public static ArrayList<Block> Flower_Pots = new ArrayList<>();

	public static void setup() {}

	public static class CustomProperties {
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
		public static final IntegerProperty COUNT_3 = IntegerProperty.create("count", 1, 3);
	}

	public static RegistryObject<Block> register(String name, Supplier<? extends Block> sup) {
		return Registry.register(name, sup);
	}

	public static RegistryObject<Block> registerWithItem(String name, Supplier<? extends Block> sup) {
		return registerWithItem(name, sup, new Item.Properties());
	}

	public static RegistryObject<Block> registerWithItem(String name, Supplier<? extends Block> sup, Item.Properties props) {
		RegistryObject<Block> registeredBlock = register(name, sup);
		FurnishItems.Registry.register(name, () -> new BlockItem(registeredBlock.get(), props));
		return registeredBlock;
	}

	public static final RegistryObject<Block> Furniture_Workbench = registerWithItem("furniture_workbench", () -> new FurnitureWorkbench());
	public static final RegistryObject<Block> Book_Pile = registerWithItem("book_pile", () -> new BookPile(BlockBehaviour.Properties.of(Material.WOOL).strength(0.2f)));

	public static final RegistryObject<Block> Oak_Table = registerWithItem("oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Square_Table = registerWithItem("oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Pedestal_Table = registerWithItem("oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), false));
	public static final RegistryObject<Block> Oak_Bedside_Table = registerWithItem("oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Oak_Kitchen_Cabinet = registerWithItem("oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Oak_Cabinet = registerWithItem("oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Oak_Wardrobe = registerWithItem("oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Oak_Stool = registerWithItem("oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Oak_Chair = registerWithItem("oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Oak_Shutter = registerWithItem("oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final RegistryObject<Block> Oak_Crate = registerWithItem("oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Oak_Shelf = registerWithItem("oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Bench = registerWithItem("oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Log_Bench = registerWithItem("oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Ladder = registerWithItem("oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Birch_Table = registerWithItem("birch_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Square_Table = registerWithItem("birch_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Pedestal_Table = registerWithItem("birch_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), false));
	public static final RegistryObject<Block> Birch_Bedside_Table = registerWithItem("birch_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Birch_Kitchen_Cabinet = registerWithItem("birch_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Birch_Cabinet = registerWithItem("birch_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Birch_Wardrobe = registerWithItem("birch_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Birch_Stool = registerWithItem("birch_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Birch_Chair = registerWithItem("birch_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Birch_Shutter = registerWithItem("birch_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final RegistryObject<Block> Birch_Crate = registerWithItem("birch_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Birch_Shelf = registerWithItem("birch_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Bench = registerWithItem("birch_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Log_Bench = registerWithItem("birch_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Ladder = registerWithItem("birch_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Acacia_Table = registerWithItem("acacia_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Square_Table = registerWithItem("acacia_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Pedestal_Table = registerWithItem("acacia_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), false));
	public static final RegistryObject<Block> Acacia_Bedside_Table = registerWithItem("acacia_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Acacia_Kitchen_Cabinet = registerWithItem("acacia_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Acacia_Cabinet = registerWithItem("acacia_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Acacia_Wardrobe = registerWithItem("acacia_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Acacia_Stool = registerWithItem("acacia_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Acacia_Chair = registerWithItem("acacia_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistryObject<Block> Acacia_Shutter = registerWithItem("acacia_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final RegistryObject<Block> Acacia_Crate = registerWithItem("acacia_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Acacia_Shelf = registerWithItem("acacia_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Bench = registerWithItem("acacia_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Log_Bench = registerWithItem("acacia_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Ladder = registerWithItem("acacia_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Jungle_Table = registerWithItem("jungle_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Square_Table = registerWithItem("jungle_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Pedestal_Table = registerWithItem("jungle_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), false));
	public static final RegistryObject<Block> Jungle_Bedside_Table = registerWithItem("jungle_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Jungle_Kitchen_Cabinet = registerWithItem("jungle_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Jungle_Cabinet = registerWithItem("jungle_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Jungle_Wardrobe = registerWithItem("jungle_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Jungle_Stool = registerWithItem("jungle_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Jungle_Chair = registerWithItem("jungle_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Jungle_Shutter = registerWithItem("jungle_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final RegistryObject<Block> Jungle_Crate = registerWithItem("jungle_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Jungle_Shelf = registerWithItem("jungle_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Bench = registerWithItem("jungle_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Log_Bench = registerWithItem("jungle_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Ladder = registerWithItem("jungle_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Spruce_Table = registerWithItem("spruce_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Square_Table = registerWithItem("spruce_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Pedestal_Table = registerWithItem("spruce_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), false));
	public static final RegistryObject<Block> Spruce_Bedside_Table = registerWithItem("spruce_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Spruce_Kitchen_Cabinet = registerWithItem("spruce_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Spruce_Cabinet = registerWithItem("spruce_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Spruce_Wardrobe = registerWithItem("spruce_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Spruce_Stool = registerWithItem("spruce_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Spruce_Chair = registerWithItem("spruce_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistryObject<Block> Spruce_Shutter = registerWithItem("spruce_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final RegistryObject<Block> Spruce_Crate = registerWithItem("spruce_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Spruce_Shelf = registerWithItem("spruce_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Bench = registerWithItem("spruce_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Log_Bench = registerWithItem("spruce_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Ladder = registerWithItem("spruce_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Dark_Oak_Table = registerWithItem("dark_oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Square_Table = registerWithItem("dark_oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Pedestal_Table = registerWithItem("dark_oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), false));
	public static final RegistryObject<Block> Dark_Oak_Bedside_Table = registerWithItem("dark_oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Dark_Oak_Kitchen_Cabinet = registerWithItem("dark_oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Dark_Oak_Cabinet = registerWithItem("dark_oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Dark_Oak_Wardrobe = registerWithItem("dark_oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Dark_Oak_Stool = registerWithItem("dark_oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Dark_Oak_Chair = registerWithItem("dark_oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final RegistryObject<Block> Dark_Oak_Shutter = registerWithItem("dark_oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final RegistryObject<Block> Dark_Oak_Crate = registerWithItem("dark_oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Dark_Oak_Shelf = registerWithItem("dark_oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Bench = registerWithItem("dark_oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Log_Bench = registerWithItem("dark_oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Ladder = registerWithItem("dark_oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Crimson_Drawers = registerWithItem("crimson_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Crimson_Cabinet = registerWithItem("crimson_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Crimson_Wardrobe = registerWithItem("crimson_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Crimson_Stool = registerWithItem("crimson_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Crimson_Chair = registerWithItem("crimson_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Crimson_Table = registerWithItem("crimson_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Square_Table = registerWithItem("crimson_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Pedestal_Table = registerWithItem("crimson_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Shutter = registerWithItem("crimson_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR)));
	public static final RegistryObject<Block> Crimson_Crate = registerWithItem("crimson_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Crimson_Shelf = registerWithItem("crimson_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Bench = registerWithItem("crimson_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Log_Bench = registerWithItem("crimson_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Ladder = registerWithItem("crimson_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Warped_Drawers = registerWithItem("warped_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Warped_Cabinet = registerWithItem("warped_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Warped_Wardrobe = registerWithItem("warped_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Warped_Stool = registerWithItem("warped_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Warped_Chair = registerWithItem("warped_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Warped_Table = registerWithItem("warped_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Square_Table = registerWithItem("warped_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Pedestal_Table = registerWithItem("warped_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Shutter = registerWithItem("warped_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.WARPED_TRAPDOOR)));
	public static final RegistryObject<Block> Warped_Crate = registerWithItem("warped_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)), new Item.Properties().stacksTo(1));
	public static final RegistryObject<Block> Warped_Shelf = registerWithItem("warped_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Bench = registerWithItem("warped_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Log_Bench = registerWithItem("warped_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Ladder = registerWithItem("warped_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Small_Locker = registerWithItem("small_locker", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));
	public static final RegistryObject<Block> Locker = registerWithItem("locker", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));

	public static final RegistryObject<Block> Red_Bunting = registerWithItem("red_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Yellow_Bunting = registerWithItem("yellow_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Green_Bunting = registerWithItem("green_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Lantern_Bunting = register("lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 15)));
	public static final RegistryObject<Block> Soul_Lantern_Bunting = register("soul_lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 10)));

	public static final RegistryObject<Block> Metal_Mailbox = registerWithItem("metal_mailbox", () -> new Mailbox(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL)));

	public static final RegistryObject<Block> Brick_Chimney_Conduit = registerWithItem("brick_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Blackstone_Chimney_Conduit = registerWithItem("blackstone_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Stone_Bricks_Chimney_Conduit = registerWithItem("stone_bricks_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Chimney_Cap = registerWithItem("chimney_cap", () -> new ChimneyCap(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final RegistryObject<Block> Disk_Rack = registerWithItem("disk_rack", () -> new DiskRack(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	// public static final RegistryObject<Block> Heavy_Metal = registerWithItem("heavy_metal", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0f, 8.0f)));

	// public static final RegistryObject<Block> Paper_Sheet = registerWithItem("paper_sheet", () -> new Paper(BlockBehaviour.Properties.of(Material.DECORATION)));

	public static final RegistryObject<Block> Recycle_Bin = registerWithItem("recycle_bin", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).strength(0.5f), FurnishData.Sounds.Recycle_Bin_Empty));
	public static final RegistryObject<Block> Trash_Can = registerWithItem("trash_can", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.0f), FurnishData.Sounds.Trash_Can_Empty));

	// Halloween Update
	public static final RegistryObject<Block> Oak_Coffin = registerWithItem("oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Birch_Coffin = registerWithItem("birch_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Spruce_Coffin = registerWithItem("spruce_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Acacia_Coffin = registerWithItem("acacia_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Coffin = registerWithItem("dark_oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Jungle_Coffin = registerWithItem("jungle_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Gravestone = registerWithItem("gravestone", () -> new VerticalSlab(BlockBehaviour.Properties.copy(Blocks.ANDESITE)));
	public static final RegistryObject<Block> Iron_Bars_Top = registerWithItem("iron_bars_top", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistryObject<Block> Iron_Gate = registerWithItem("iron_gate", () -> new IronGate(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistryObject<Block> Skull_Torch = registerWithItem("skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 10), ParticleTypes.FLAME));
	public static final RegistryObject<Block> Wither_Skull_Torch = registerWithItem("wither_skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 7), ParticleTypes.SOUL_FIRE_FLAME));
	public static final RegistryObject<Block> Cobweb = registerWithItem("cobweb", () -> new Cobweb(BlockBehaviour.Properties.of(Material.WEB).strength(0.0f)));

	public static final RegistryObject<Block> Picture_Frame = registerWithItem("picture_frame", () -> new PictureFrame(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(SoundType.SCAFFOLDING).noCollission().noOcclusion()));
	// public static final RegistryObject<Block> Flower_Pot = registerWithItem("flower_pot", () -> new FlowerPot(BlockBehaviour.Properties.of(Material.CLAY).strength(1.0f).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops(), 1));

	// Winter
	public static final RegistryObject<Block> Snow_On_Fence = register("snow_on_fence", () -> new SnowOnFence(BlockBehaviour.Properties.copy(Blocks.SNOW)));
	public static final RegistryObject<Block> Snow_On_Stairs = register("snow_on_stairs", () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(Blocks.SNOW), Blocks.SNOW));

	public static final RegistryObject<Block> Chess_Board = registerWithItem("chess_board", () -> new ChessBoard(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(.5f)));

	public static final String[] Rare_Plates_Names = {"chinese", "english"};

	public static HashMap<String, RegistryObject<Block>> Carpets_On_Stairs = new HashMap<>(16);
	public static HashMap<String, RegistryObject<Block>> Carpets_On_Trapdoors = new HashMap<>(16);

	public static ArrayList<RegistryObject<Block>> Amphorae = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Sofas = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Awnings = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Curtains = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Plates = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Rare_Plates = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Showcases = new ArrayList<>();
	//public static ArrayList<RegistryObject<Block>> Animal_Baskets = new ArrayList<>();
	public static ArrayList<RegistryObject<Block>> Paper_Lamps = new ArrayList<>();

	static {
		Amphorae.add(register("amphora", () -> new Amphora(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		Plates.add(register("plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		//Animal_Baskets.add(register("white_animal_basket", () -> new AnimalBasket(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL))));

		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, register(String.format("%s_carpet_on_stairs", color), () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, register(String.format("%s_carpet_on_trapdoor", color), () -> new CarpetOnTrapdoor(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings.add(registerWithItem(String.format("%s_awning", color), () -> new Awning(BlockBehaviour.Properties.copy(coloredCarpet))));
			Curtains.add(registerWithItem(String.format("%s_curtain", color), () -> new Curtain(BlockBehaviour.Properties.copy(coloredCarpet))));

			Block coloredWool = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas.add(registerWithItem(String.format("%s_sofa", color), () -> new Sofa(BlockBehaviour.Properties.copy(coloredWool))));
			Showcases.add(registerWithItem(String.format("%s_showcase", color), () -> new Showcase(BlockBehaviour.Properties.copy(Blocks.GLASS))));

			Block coloredTerracotta = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae.add(registerWithItem(String.format("%s_amphora", color), () -> new Amphora(BlockBehaviour.Properties.copy(coloredTerracotta))));
			Plates.add(registerWithItem(String.format("%s_plate", color), () -> new Plate(BlockBehaviour.Properties.copy(coloredTerracotta)), new Item.Properties().stacksTo(16)));

			Paper_Lamps.add(registerWithItem(String.format("%s_paper_lamp", color), () -> new PaperLamp()));
		}

		for(String s : Rare_Plates_Names) {
			Rare_Plates.add(registerWithItem(String.format("rare_%s_plate", s), () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)), new Item.Properties().stacksTo(16).rarity(Rarity.RARE)));
		}

		Plates.addAll(Rare_Plates);
	}
}
