package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.block.Cobweb;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FurnishBlocks {
	public static final DeferredRegister<Block> Registry = DeferredRegister.create(ForgeRegistries.BLOCKS, Furnish.MODID);

	public static ArrayList<Block> Furniture_3x9 = new ArrayList<>();
	public static ArrayList<Block> Furniture_6x9 = new ArrayList<>();
	public static ArrayList<Block> Shelves = new ArrayList<>();
	public static ArrayList<Block> Recycle_Bins = new ArrayList<>();

	public static class CustomProperties {
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
		public static final IntegerProperty COUNT_3 = IntegerProperty.create("count", 1, 3);
	}

	public static final RegistryObject<Block> Furniture_Workbench = Registry.register("furniture_workbench", () -> new FurnitureWorkbench());
	public static final RegistryObject<Block> Book_Pile = Registry.register("book_pile", () -> new BookPile(BlockBehaviour.Properties.of(Material.WOOL).strength(0.2f)));

	public static final RegistryObject<Block> Oak_Table = Registry.register("oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Square_Table = Registry.register("oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Pedestal_Table = Registry.register("oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Bedside_Table = Registry.register("oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Oak_Kitchen_Cabinet = Registry.register("oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Oak_Cabinet = Registry.register("oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Oak_Wardrobe = Registry.register("oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Oak_Stool = Registry.register("oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Oak_Chair = Registry.register("oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Oak_Shutter = Registry.register("oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final RegistryObject<Block> Oak_Crate = Registry.register("oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Shelf = Registry.register("oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Bench = Registry.register("oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Log_Bench = Registry.register("oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Oak_Ladder = Registry.register("oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Birch_Table = Registry.register("birch_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Square_Table = Registry.register("birch_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Pedestal_Table = Registry.register("birch_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Bedside_Table = Registry.register("birch_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Birch_Kitchen_Cabinet = Registry.register("birch_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Birch_Cabinet = Registry.register("birch_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Birch_Wardrobe = Registry.register("birch_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Birch_Stool = Registry.register("birch_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Birch_Chair = Registry.register("birch_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Birch_Shutter = Registry.register("birch_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final RegistryObject<Block> Birch_Crate = Registry.register("birch_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Shelf = Registry.register("birch_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Bench = Registry.register("birch_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Log_Bench = Registry.register("birch_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Birch_Ladder = Registry.register("birch_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Acacia_Table = Registry.register("acacia_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Square_Table = Registry.register("acacia_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Pedestal_Table = Registry.register("acacia_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Bedside_Table = Registry.register("acacia_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Acacia_Kitchen_Cabinet = Registry.register("acacia_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Acacia_Cabinet = Registry.register("acacia_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Acacia_Wardrobe = Registry.register("acacia_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Acacia_Stool = Registry.register("acacia_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Acacia_Chair = Registry.register("acacia_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistryObject<Block> Acacia_Shutter = Registry.register("acacia_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final RegistryObject<Block> Acacia_Crate = Registry.register("acacia_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Shelf = Registry.register("acacia_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Bench = Registry.register("acacia_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Log_Bench = Registry.register("acacia_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Acacia_Ladder = Registry.register("acacia_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Jungle_Table = Registry.register("jungle_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Square_Table = Registry.register("jungle_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Pedestal_Table = Registry.register("jungle_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Bedside_Table = Registry.register("jungle_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Jungle_Kitchen_Cabinet = Registry.register("jungle_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Jungle_Cabinet = Registry.register("jungle_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Jungle_Wardrobe = Registry.register("jungle_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Jungle_Stool = Registry.register("jungle_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Jungle_Chair = Registry.register("jungle_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Jungle_Shutter = Registry.register("jungle_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final RegistryObject<Block> Jungle_Crate = Registry.register("jungle_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Shelf = Registry.register("jungle_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Bench = Registry.register("jungle_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Log_Bench = Registry.register("jungle_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Jungle_Ladder = Registry.register("jungle_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Spruce_Table = Registry.register("spruce_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Square_Table = Registry.register("spruce_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Pedestal_Table = Registry.register("spruce_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Bedside_Table = Registry.register("spruce_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Spruce_Kitchen_Cabinet = Registry.register("spruce_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Spruce_Cabinet = Registry.register("spruce_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Spruce_Wardrobe = Registry.register("spruce_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Spruce_Stool = Registry.register("spruce_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Spruce_Chair = Registry.register("spruce_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final RegistryObject<Block> Spruce_Shutter = Registry.register("spruce_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final RegistryObject<Block> Spruce_Crate = Registry.register("spruce_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Shelf = Registry.register("spruce_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Bench = Registry.register("spruce_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Log_Bench = Registry.register("spruce_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Spruce_Ladder = Registry.register("spruce_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Dark_Oak_Table = Registry.register("dark_oak_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Square_Table = Registry.register("dark_oak_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Pedestal_Table = Registry.register("dark_oak_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Bedside_Table = Registry.register("dark_oak_bedside_table", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Dark_Oak_Kitchen_Cabinet = Registry.register("dark_oak_kitchen_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Dark_Oak_Cabinet = Registry.register("dark_oak_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Dark_Oak_Wardrobe = Registry.register("dark_oak_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final RegistryObject<Block> Dark_Oak_Stool = Registry.register("dark_oak_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Dark_Oak_Chair = Registry.register("dark_oak_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final RegistryObject<Block> Dark_Oak_Shutter = Registry.register("dark_oak_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final RegistryObject<Block> Dark_Oak_Crate = Registry.register("dark_oak_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Shelf = Registry.register("dark_oak_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Bench = Registry.register("dark_oak_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Log_Bench = Registry.register("dark_oak_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Ladder = Registry.register("dark_oak_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Crimson_Drawers = Registry.register("crimson_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Crimson_Cabinet = Registry.register("crimson_cabinet", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Crimson_Wardrobe = Registry.register("crimson_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Crimson_Stool = Registry.register("crimson_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Crimson_Chair = Registry.register("crimson_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Crimson_Table = Registry.register("crimson_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Square_Table = Registry.register("crimson_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Pedestal_Table = Registry.register("crimson_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Shutter = Registry.register("crimson_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.CRIMSON_TRAPDOOR)));
	public static final RegistryObject<Block> Crimson_Crate = Registry.register("crimson_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Shelf = Registry.register("crimson_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Bench = Registry.register("crimson_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Log_Bench = Registry.register("crimson_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS)));
	public static final RegistryObject<Block> Crimson_Ladder = Registry.register("crimson_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Warped_Drawers = Registry.register("warped_drawers", () -> new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final RegistryObject<Block> Warped_Cabinet = Registry.register("warped_cabinet", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Warped_Wardrobe = Registry.register("warped_wardrobe", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final RegistryObject<Block> Warped_Stool = Registry.register("warped_stool", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), Chair.BASE_SHAPES));
	public static final RegistryObject<Block> Warped_Chair = Registry.register("warped_chair", () -> new Chair(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final RegistryObject<Block> Warped_Table = Registry.register("warped_table", () -> new Table(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Square_Table = Registry.register("warped_square_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Pedestal_Table = Registry.register("warped_pedestal_table", () -> new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Shutter = Registry.register("warped_shutter", () -> new Shutter(BlockBehaviour.Properties.copy(Blocks.WARPED_TRAPDOOR)));
	public static final RegistryObject<Block> Warped_Crate = Registry.register("warped_crate", () -> new Crate(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Shelf = Registry.register("warped_shelf", () -> new Shelf(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Bench = Registry.register("warped_bench", () -> new Bench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Log_Bench = Registry.register("warped_log_bench", () -> new LogBench(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS)));
	public static final RegistryObject<Block> Warped_Ladder = Registry.register("warped_ladder", () -> new Ladder(BlockBehaviour.Properties.of(Material.DECORATION).strength(0.4F).sound(SoundType.LADDER)));

	public static final RegistryObject<Block> Small_Locker = Registry.register("small_locker", () -> new Cabinet(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));
	public static final RegistryObject<Block> Locker = Registry.register("locker", () -> new Wardrobe(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).requiresCorrectToolForDrops(), FurnishData.Sounds.Locker_Open, FurnishData.Sounds.Locker_Close));

	public static final RegistryObject<Block> Red_Bunting = Registry.register("red_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Yellow_Bunting = Registry.register("yellow_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Green_Bunting = Registry.register("green_bunting", () -> new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final RegistryObject<Block> Lantern_Bunting = Registry.register("lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 15)));
	public static final RegistryObject<Block> Soul_Lantern_Bunting = Registry.register("soul_lantern_bunting", () -> new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 10)));

	public static final RegistryObject<Block> Metal_Mailbox = Registry.register("metal_mailbox", () -> new Mailbox(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL)));

	public static final RegistryObject<Block> Brick_Chimney_Conduit = Registry.register("brick_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Blackstone_Chimney_Conduit = Registry.register("blackstone_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Stone_Bricks_Chimney_Conduit = Registry.register("stone_bricks_chimney_conduit", () -> new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final RegistryObject<Block> Chimney_Cap = Registry.register("chimney_cap", () -> new ChimneyCap(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final RegistryObject<Block> Disk_Rack = Registry.register("disk_rack", () -> new DiskRack(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	// public static final RegistryObject<Block> Heavy_Metal = Registry.register("heavy_metal", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0f, 8.0f)));

	// public static final RegistryObject<Block> Paper_Sheet = Registry.register("paper_sheet", () -> new Paper(BlockBehaviour.Properties.of(Material.DECORATION)));

	public static final RegistryObject<Block> Recycle_Bin = Registry.register("recycle_bin", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.SCAFFOLDING).strength(0.5f), FurnishData.Sounds.Recycle_Bin_Empty));
	public static final RegistryObject<Block> Trash_Can = Registry.register("trash_can", () -> new RecycleBin(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1.0f), FurnishData.Sounds.Trash_Can_Empty));

	// Halloween Update
	public static final RegistryObject<Block> Oak_Coffin = Registry.register("oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final RegistryObject<Block> Birch_Coffin = Registry.register("birch_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final RegistryObject<Block> Spruce_Coffin = Registry.register("spruce_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final RegistryObject<Block> Acacia_Coffin = Registry.register("acacia_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final RegistryObject<Block> Dark_Oak_Coffin = Registry.register("dark_oak_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final RegistryObject<Block> Jungle_Coffin = Registry.register("jungle_coffin", () -> new Coffin(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final RegistryObject<Block> Gravestone = Registry.register("gravestone", () -> new VerticalSlab(BlockBehaviour.Properties.copy(Blocks.ANDESITE)));
	public static final RegistryObject<Block> Iron_Bars_Top = Registry.register("iron_bars_top", () -> new IronBarsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistryObject<Block> Iron_Gate = Registry.register("iron_gate", () -> new IronGate(BlockBehaviour.Properties.copy(Blocks.IRON_BARS)));
	public static final RegistryObject<Block> Skull_Torch = Registry.register("skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 10), ParticleTypes.FLAME));
	public static final RegistryObject<Block> Wither_Skull_Torch = Registry.register("wither_skull_torch", () -> new SkullTorch(BlockBehaviour.Properties.copy(Blocks.SKELETON_SKULL).lightLevel((state) -> 7), ParticleTypes.SOUL_FIRE_FLAME));
	public static final RegistryObject<Block> Cobweb = Registry.register("cobweb", () -> new Cobweb(BlockBehaviour.Properties.of(Material.WEB).strength(0.0f)));

	public static final RegistryObject<Block> Picture_Frame = Registry.register("picture_frame", () -> new PictureFrame(BlockBehaviour.Properties.of(Material.WOOD).instabreak().sound(SoundType.SCAFFOLDING).noCollission().noOcclusion()));

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
		Amphorae.add(Registry.register("amphora", () -> new Amphora(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		Plates.add(Registry.register("plate", () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		//Animal_Baskets.add(Registry.register("white_animal_basket", () -> new AnimalBasket(BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL))));

		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, Registry.register(String.format("%s_carpet_on_stairs", color), () -> new CarpetOnStairs(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, Registry.register(String.format("%s_carpet_on_trapdoor", color), () -> new CarpetOnTrapdoor(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings.add(Registry.register(String.format("%s_awning", color), () -> new Awning(BlockBehaviour.Properties.copy(coloredCarpet))));
			Curtains.add(Registry.register(String.format("%s_curtain", color), () -> new Curtain(BlockBehaviour.Properties.copy(coloredCarpet))));

			Block coloredWool = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas.add(Registry.register(String.format("%s_sofa", color), () -> new Sofa(BlockBehaviour.Properties.copy(coloredWool))));
			Showcases.add(Registry.register(String.format("%s_showcase", color), () -> new Showcase(BlockBehaviour.Properties.copy(Blocks.GLASS))));

			Block coloredTerracotta = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae.add(Registry.register(String.format("%s_amphora", color), () -> new Amphora(BlockBehaviour.Properties.copy(coloredTerracotta))));
			Plates.add(Registry.register(String.format("%s_plate", color), () -> new Plate(BlockBehaviour.Properties.copy(coloredTerracotta))));

			Paper_Lamps.add(Registry.register(String.format("%s_paper_lamp", color), () -> new PaperLamp()));
		}

		for(String s : Rare_Plates_Names) {
			Rare_Plates.add(Registry.register(String.format("rare_%s_plate", s), () -> new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA))));
		}

		Plates.addAll(Rare_Plates);
	}

	public static void clientSetup() {
		ItemBlockRenderTypes.setRenderLayer(Red_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Yellow_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Green_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Jungle_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Acacia_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Crimson_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Warped_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Recycle_Bin.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Iron_Bars_Top.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Iron_Gate.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Cobweb.get(), RenderType.translucent());
		// ItemBlockRenderTypes.setRenderLayer(Paper_Sheet.get(), RenderType.translucent());
		for(RegistryObject<Block> b : Showcases) ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.translucent());
	}
}
