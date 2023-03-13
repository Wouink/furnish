package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
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

public class FurnishBlocks {
	public static ArrayList<Block> Furniture_3x9 = new ArrayList<>();
	public static ArrayList<Block> Furniture_6x9 = new ArrayList<>();
	public static ArrayList<Block> Shelves = new ArrayList<>();
	public static ArrayList<Block> Recycle_Bins = new ArrayList<>();
	public static ArrayList<Block> Flower_Pots = new ArrayList<>();

	public static class CustomProperties {
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
		public static final IntegerProperty COUNT_3 = IntegerProperty.create("count", 1, 3);
	}

	public static final RegistrySupplier<Block> Furniture_Workbench = FurnishRegistries.registerBlockWithItem("furniture_workbench", () -> new FurnitureWorkbench());
	public static final RegistrySupplier<Block> Book_Pile = FurnishRegistries.registerBlockWithItem("book_pile", () -> new BookPile(BlockBehaviour.Properties.of(Material.WOOL).strength(0.2f)));

	public static final RegistrySupplier<Block> Oak_Table = FurnishRegistries.registerBlockWithItem("oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Square_Table = FurnishRegistries.registerBlockWithItem("oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Pedestal_Table = FurnishRegistries.registerBlockWithItem("oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), false));
	public static final RegistrySupplier<Block> Oak_Bedside_Table = FurnishRegistries.registerBlockWithItem("oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Oak_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Oak_Cabinet = FurnishRegistries.registerBlockWithItem("oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Oak_Wardrobe = FurnishRegistries.registerBlockWithItem("oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Oak_Stool = FurnishRegistries.registerBlockWithItem("oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Oak_Chair = FurnishRegistries.registerBlockWithItem("oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Oak_Shutter = FurnishRegistries.registerBlockWithItem("oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final RegistrySupplier<Block> Oak_Crate = FurnishRegistries.registerBlockWithItem("oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Shelf = FurnishRegistries.registerBlockWithItem("oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Bench = FurnishRegistries.registerBlockWithItem("oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Log_Bench = FurnishRegistries.registerBlockWithItem("oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Oak_Ladder = FurnishRegistries.registerBlockWithItem("oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Birch_Table = FurnishRegistries.registerBlockWithItem("birch_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Square_Table = FurnishRegistries.registerBlockWithItem("birch_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Pedestal_Table = FurnishRegistries.registerBlockWithItem("birch_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), false));
	public static final RegistrySupplier<Block> Birch_Bedside_Table = FurnishRegistries.registerBlockWithItem("birch_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Birch_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("birch_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Birch_Cabinet = FurnishRegistries.registerBlockWithItem("birch_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Birch_Wardrobe = FurnishRegistries.registerBlockWithItem("birch_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Birch_Stool = FurnishRegistries.registerBlockWithItem("birch_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Birch_Chair = FurnishRegistries.registerBlockWithItem("birch_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Birch_Shutter = FurnishRegistries.registerBlockWithItem("birch_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final RegistrySupplier<Block> Birch_Crate = FurnishRegistries.registerBlockWithItem("birch_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Shelf = FurnishRegistries.registerBlockWithItem("birch_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Bench = FurnishRegistries.registerBlockWithItem("birch_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Log_Bench = FurnishRegistries.registerBlockWithItem("birch_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Ladder = FurnishRegistries.registerBlockWithItem("birch_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Acacia_Table = FurnishRegistries.registerBlockWithItem("acacia_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Square_Table = FurnishRegistries.registerBlockWithItem("acacia_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Pedestal_Table = FurnishRegistries.registerBlockWithItem("acacia_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), false));
	public static final RegistrySupplier<Block> Acacia_Bedside_Table = FurnishRegistries.registerBlockWithItem("acacia_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Acacia_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("acacia_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Acacia_Cabinet = FurnishRegistries.registerBlockWithItem("acacia_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Acacia_Wardrobe = FurnishRegistries.registerBlockWithItem("acacia_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Acacia_Stool = FurnishRegistries.registerBlockWithItem("acacia_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Acacia_Chair = FurnishRegistries.registerBlockWithItem("acacia_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistrySupplier<Block> Acacia_Shutter = FurnishRegistries.registerBlockWithItem("acacia_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final RegistrySupplier<Block> Acacia_Crate = FurnishRegistries.registerBlockWithItem("acacia_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Shelf = FurnishRegistries.registerBlockWithItem("acacia_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Bench = FurnishRegistries.registerBlockWithItem("acacia_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Log_Bench = FurnishRegistries.registerBlockWithItem("acacia_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Ladder = FurnishRegistries.registerBlockWithItem("acacia_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Jungle_Table = FurnishRegistries.registerBlockWithItem("jungle_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Square_Table = FurnishRegistries.registerBlockWithItem("jungle_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Pedestal_Table = FurnishRegistries.registerBlockWithItem("jungle_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), false));
	public static final RegistrySupplier<Block> Jungle_Bedside_Table = FurnishRegistries.registerBlockWithItem("jungle_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Jungle_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("jungle_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Jungle_Cabinet = FurnishRegistries.registerBlockWithItem("jungle_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Jungle_Wardrobe = FurnishRegistries.registerBlockWithItem("jungle_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Jungle_Stool = FurnishRegistries.registerBlockWithItem("jungle_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Jungle_Chair = FurnishRegistries.registerBlockWithItem("jungle_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Jungle_Shutter = FurnishRegistries.registerBlockWithItem("jungle_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final RegistrySupplier<Block> Jungle_Crate = FurnishRegistries.registerBlockWithItem("jungle_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Shelf = FurnishRegistries.registerBlockWithItem("jungle_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Bench = FurnishRegistries.registerBlockWithItem("jungle_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Log_Bench = FurnishRegistries.registerBlockWithItem("jungle_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Ladder = FurnishRegistries.registerBlockWithItem("jungle_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Spruce_Table = FurnishRegistries.registerBlockWithItem("spruce_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Square_Table = FurnishRegistries.registerBlockWithItem("spruce_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Pedestal_Table = FurnishRegistries.registerBlockWithItem("spruce_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), false));
	public static final RegistrySupplier<Block> Spruce_Bedside_Table = FurnishRegistries.registerBlockWithItem("spruce_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Spruce_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("spruce_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Spruce_Cabinet = FurnishRegistries.registerBlockWithItem("spruce_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistrySupplier<Block> Spruce_Wardrobe = FurnishRegistries.registerBlockWithItem("spruce_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistrySupplier<Block> Spruce_Stool = FurnishRegistries.registerBlockWithItem("spruce_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Spruce_Chair = FurnishRegistries.registerBlockWithItem("spruce_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistrySupplier<Block> Spruce_Shutter = FurnishRegistries.registerBlockWithItem("spruce_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final RegistrySupplier<Block> Spruce_Crate = FurnishRegistries.registerBlockWithItem("spruce_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Shelf = FurnishRegistries.registerBlockWithItem("spruce_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Bench = FurnishRegistries.registerBlockWithItem("spruce_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Log_Bench = FurnishRegistries.registerBlockWithItem("spruce_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Ladder = FurnishRegistries.registerBlockWithItem("spruce_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Dark_Oak_Table = FurnishRegistries.registerBlockWithItem("dark_oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Square_Table = FurnishRegistries.registerBlockWithItem("dark_oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Pedestal_Table = FurnishRegistries.registerBlockWithItem("dark_oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), false));
	public static final RegistrySupplier<Block> Dark_Oak_Bedside_Table = FurnishRegistries.registerBlockWithItem("dark_oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Dark_Oak_Kitchen_Cabinet = FurnishRegistries.registerBlockWithItem("dark_oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Dark_Oak_Cabinet = FurnishRegistries.registerBlockWithItem("dark_oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistrySupplier<Block> Dark_Oak_Wardrobe = FurnishRegistries.registerBlockWithItem("dark_oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistrySupplier<Block> Dark_Oak_Stool = FurnishRegistries.registerBlockWithItem("dark_oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Dark_Oak_Chair = FurnishRegistries.registerBlockWithItem("dark_oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final RegistrySupplier<Block> Dark_Oak_Shutter = FurnishRegistries.registerBlockWithItem("dark_oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final RegistrySupplier<Block> Dark_Oak_Crate = FurnishRegistries.registerBlockWithItem("dark_oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Shelf = FurnishRegistries.registerBlockWithItem("dark_oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Bench = FurnishRegistries.registerBlockWithItem("dark_oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Log_Bench = FurnishRegistries.registerBlockWithItem("dark_oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Ladder = FurnishRegistries.registerBlockWithItem("dark_oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Crimson_Drawers = FurnishRegistries.registerBlockWithItem("crimson_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Crimson_Cabinet = FurnishRegistries.registerBlockWithItem("crimson_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Crimson_Wardrobe = FurnishRegistries.registerBlockWithItem("crimson_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Crimson_Stool = FurnishRegistries.registerBlockWithItem("crimson_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Crimson_Chair = FurnishRegistries.registerBlockWithItem("crimson_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Crimson_Table = FurnishRegistries.registerBlockWithItem("crimson_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Square_Table = FurnishRegistries.registerBlockWithItem("crimson_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Pedestal_Table = FurnishRegistries.registerBlockWithItem("crimson_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Shutter = FurnishRegistries.registerBlockWithItem("crimson_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR)));
	public static final RegistrySupplier<Block> Crimson_Crate = FurnishRegistries.registerBlockWithItem("crimson_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Shelf = FurnishRegistries.registerBlockWithItem("crimson_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Bench = FurnishRegistries.registerBlockWithItem("crimson_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Log_Bench = FurnishRegistries.registerBlockWithItem("crimson_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistrySupplier<Block> Crimson_Ladder = FurnishRegistries.registerBlockWithItem("crimson_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Warped_Drawers = FurnishRegistries.registerBlockWithItem("warped_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistrySupplier<Block> Warped_Cabinet = FurnishRegistries.registerBlockWithItem("warped_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Warped_Wardrobe = FurnishRegistries.registerBlockWithItem("warped_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistrySupplier<Block> Warped_Stool = FurnishRegistries.registerBlockWithItem("warped_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), Chair.BASE_SHAPES));
	public static final RegistrySupplier<Block> Warped_Chair = FurnishRegistries.registerBlockWithItem("warped_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistrySupplier<Block> Warped_Table = FurnishRegistries.registerBlockWithItem("warped_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Square_Table = FurnishRegistries.registerBlockWithItem("warped_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Pedestal_Table = FurnishRegistries.registerBlockWithItem("warped_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Shutter = FurnishRegistries.registerBlockWithItem("warped_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.WARPED_TRAPDOOR)));
	public static final RegistrySupplier<Block> Warped_Crate = FurnishRegistries.registerBlockWithItem("warped_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Shelf = FurnishRegistries.registerBlockWithItem("warped_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Bench = FurnishRegistries.registerBlockWithItem("warped_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Log_Bench = FurnishRegistries.registerBlockWithItem("warped_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistrySupplier<Block> Warped_Ladder = FurnishRegistries.registerBlockWithItem("warped_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistrySupplier<Block> Small_Locker = FurnishRegistries.registerBlockWithItem("small_locker", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));
	public static final RegistrySupplier<Block> Locker = FurnishRegistries.registerBlockWithItem("locker", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));

	public static final RegistrySupplier<Block> Red_Bunting = FurnishRegistries.registerBlockWithItem("red_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Yellow_Bunting = FurnishRegistries.registerBlockWithItem("yellow_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Green_Bunting = FurnishRegistries.registerBlockWithItem("green_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistrySupplier<Block> Lantern_Bunting = FurnishRegistries.registerBlockWithItem("lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 15)));
	public static final RegistrySupplier<Block> Soul_Lantern_Bunting = FurnishRegistries.registerBlockWithItem("soul_lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 10)));

	public static final RegistrySupplier<Block> Metal_Mailbox = FurnishRegistries.registerBlockWithItem("metal_mailbox", () -> new Mailbox(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL)));

	public static final RegistrySupplier<Block> Brick_Chimney_Conduit = FurnishRegistries.registerBlockWithItem("brick_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Blackstone_Chimney_Conduit = FurnishRegistries.registerBlockWithItem("blackstone_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Stone_Bricks_Chimney_Conduit = FurnishRegistries.registerBlockWithItem("stone_bricks_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistrySupplier<Block> Chimney_Cap = FurnishRegistries.registerBlockWithItem("chimney_cap", () -> new ChimneyCap(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final RegistrySupplier<Block> Disk_Rack = FurnishRegistries.registerBlockWithItem("disk_rack", () -> new DiskRack(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	// public static final RegistrySupplier<Block> Heavy_Metal = FurnishRegistries.registerBlockWithItem("heavy_metal", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0f, 8.0f)));

	// public static final RegistrySupplier<Block> Paper_Sheet = FurnishRegistries.registerBlockWithItem("paper_sheet", () -> new Paper(BlockBehaviour.Properties.of(Material.DECORATION)));

	public static final RegistrySupplier<Block> Recycle_Bin = FurnishRegistries.registerBlockWithItem("recycle_bin", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).strength(0.5f), FurnishData.Sounds.Recycle_Bin_Empty));
	public static final RegistrySupplier<Block> Trash_Can = FurnishRegistries.registerBlockWithItem("trash_can", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.0f), FurnishData.Sounds.Trash_Can_Empty));

	// Halloween Update
	public static final RegistrySupplier<Block> Oak_Coffin = FurnishRegistries.registerBlockWithItem("oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistrySupplier<Block> Birch_Coffin = FurnishRegistries.registerBlockWithItem("birch_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistrySupplier<Block> Spruce_Coffin = FurnishRegistries.registerBlockWithItem("spruce_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistrySupplier<Block> Acacia_Coffin = FurnishRegistries.registerBlockWithItem("acacia_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistrySupplier<Block> Dark_Oak_Coffin = FurnishRegistries.registerBlockWithItem("dark_oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistrySupplier<Block> Jungle_Coffin = FurnishRegistries.registerBlockWithItem("jungle_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistrySupplier<Block> Gravestone = FurnishRegistries.registerBlockWithItem("gravestone", () -> new VerticalSlab(BlockBehaviour.Properties.copy(Blocks.ANDESITE)));
	public static final RegistrySupplier<Block> Iron_Bars_Top = FurnishRegistries.registerBlockWithItem("iron_bars_top", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistrySupplier<Block> Iron_Gate = FurnishRegistries.registerBlockWithItem("iron_gate", () -> new IronGate(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistrySupplier<Block> Skull_Torch = FurnishRegistries.registerBlockWithItem("skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 10), ParticleTypes.FLAME));
	public static final RegistrySupplier<Block> Wither_Skull_Torch = FurnishRegistries.registerBlockWithItem("wither_skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 7), ParticleTypes.SOUL_FIRE_FLAME));
	public static final RegistrySupplier<Block> Cobweb = FurnishRegistries.registerBlockWithItem("cobweb", () -> new Cobweb(BlockBehaviour.Properties.of(Material.WEB).strength(0.0f)));

	public static final RegistrySupplier<Block> Picture_Frame = FurnishRegistries.registerBlockWithItem("picture_frame", () -> new PictureFrame(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(SoundType.SCAFFOLDING).noCollission().noOcclusion()));
	// public static final RegistrySupplier<Block> Flower_Pot = FurnishRegistries.registerBlockWithItem("flower_pot", () -> new FlowerPot(BlockBehaviour.Properties.of(Material.CLAY).strength(1.0f).sound(SoundType.STONE).noOcclusion().requiresCorrectToolForDrops(), 1));

	// Winter
	public static final RegistrySupplier<Block> Snow_On_Fence = FurnishRegistries.registerBlock("snow_on_fence", () -> new SnowOnFence(BlockBehaviour.Properties.copy(Blocks.SNOW)));
	public static final RegistrySupplier<Block> Snow_On_Stairs = FurnishRegistries.registerBlock("snow_on_stairs", () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(Blocks.SNOW), Blocks.SNOW));

	public static final RegistrySupplier<Block> Chess_Board = FurnishRegistries.registerBlockWithItem("chess_board", () -> new ChessBoard(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(.5f)));

	public static final String[] Rare_Plates_Names = {"chinese", "english"};

	public static HashMap<String, RegistrySupplier<Block>> Carpets_On_Stairs = new HashMap<>(16);
	public static HashMap<String, RegistrySupplier<Block>> Carpets_On_Trapdoors = new HashMap<>(16);

	public static ArrayList<RegistrySupplier<Block>> Amphorae = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Sofas = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Awnings = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Curtains = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Plates = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Rare_Plates = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Showcases = new ArrayList<>();
	//public static ArrayList<RegistrySupplier<Block>> Animal_Baskets = new ArrayList<>();
	public static ArrayList<RegistrySupplier<Block>> Paper_Lamps = new ArrayList<>();

	static {
		Amphorae.add(FurnishRegistries.registerBlockWithItem("amphora", () -> new Amphora(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		Plates.add(FurnishRegistries.registerBlockWithItem("plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		//Animal_Baskets.add(FurnishRegistries.registerBlockWithItem("white_animal_basket", () -> new AnimalBasket(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL))));

		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, FurnishRegistries.registerBlock(String.format("%s_carpet_on_stairs", color), () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, FurnishRegistries.registerBlock(String.format("%s_carpet_on_trapdoor", color), () -> new CarpetOnTrapdoor(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings.add(FurnishRegistries.registerBlockWithItem(String.format("%s_awning", color), () -> new Awning(BlockBehaviour.Properties.copy(coloredCarpet))));
			Curtains.add(FurnishRegistries.registerBlockWithItem(String.format("%s_curtain", color), () -> new Curtain(BlockBehaviour.Properties.copy(coloredCarpet))));

			Block coloredWool = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas.add(FurnishRegistries.registerBlockWithItem(String.format("%s_sofa", color), () -> new Sofa(BlockBehaviour.Properties.copy(coloredWool))));
			Showcases.add(FurnishRegistries.registerBlockWithItem(String.format("%s_showcase", color), () -> new Showcase(BlockBehaviour.Properties.copy(Blocks.GLASS))));

			Block coloredTerracotta = Registry.BLOCK.get(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae.add(FurnishRegistries.registerBlockWithItem(String.format("%s_amphora", color), () -> new Amphora(BlockBehaviour.Properties.copy(coloredTerracotta))));
			Plates.add(FurnishRegistries.registerBlockWithItem(String.format("%s_plate", color), () -> new Plate(BlockBehaviour.Properties.copy(coloredTerracotta))));

			Paper_Lamps.add(FurnishRegistries.registerBlockWithItem(String.format("%s_paper_lamp", color), () -> new PaperLamp()));
		}

		for(String s : Rare_Plates_Names) {
			Rare_Plates.add(FurnishRegistries.registerBlockWithItem(String.format("rare_%s_plate", s), () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		}

		Plates.addAll(Rare_Plates);
	}
}
