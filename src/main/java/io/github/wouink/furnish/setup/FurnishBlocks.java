package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.DyeColor;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class FurnishBlocks {
	public static final DeferredRegister<Block> Registry = DeferredRegister.create(ForgeRegistries.BLOCKS, Furnish.MODID);

	public static class CustomProperties {
		public static final IntegerProperty NOTE = IntegerProperty.create("note", 0, 12);
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	}

	public static final Block Furniture_Workbench = register("furniture_workbench", new FurnitureWorkbench());
	public static final Block Book_Pile = register("book_pile", new BookPile(AbstractBlock.Properties.of(Material.WOOL).strength(0.2f)));

	public static final Block Oak_Table = register("oak_table", new Table(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Square_Table = register("oak_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Bedside_Table = register("oak_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Oak_Kitchen_Cabinet = register("oak_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Oak_Cabinet = register("oak_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Oak_Wardrobe = register("oak_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Oak_Stool = register("oak_stool", new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final Block Oak_Chair = register("oak_chair", new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Oak_Shutter = register("oak_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final Block Oak_Crate = register("oak_crate", new Crate(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Shelf = register("oak_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Potion_Shelf = register("oak_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS)));
	
	public static final Block Birch_Table = register("birch_table", new Table(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Square_Table = register("birch_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Bedside_Table = register("birch_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Birch_Kitchen_Cabinet = register("birch_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Birch_Cabinet = register("birch_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Birch_Wardrobe = register("birch_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Birch_Stool = register("birch_stool", new Chair(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final Block Birch_Chair = register("birch_chair", new Chair(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Birch_Shutter = register("birch_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final Block Birch_Crate = register("birch_crate", new Crate(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Shelf = register("birch_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Potion_Shelf = register("birch_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS)));

	public static final Block Acacia_Table = register("acacia_table", new Table(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Square_Table = register("acacia_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Bedside_Table = register("acacia_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Acacia_Kitchen_Cabinet = register("acacia_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Acacia_Cabinet = register("acacia_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Acacia_Wardrobe = register("acacia_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Acacia_Stool = register("acacia_stool", new Chair(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final Block Acacia_Chair = register("acacia_chair", new Chair(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final Block Acacia_Shutter = register("acacia_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final Block Acacia_Crate = register("acacia_crate", new Crate(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Shelf = register("acacia_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Potion_Shelf = register("acacia_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final Block Jungle_Table = register("jungle_table", new Table(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Square_Table = register("jungle_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Bedside_Table = register("jungle_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Jungle_Kitchen_Cabinet = register("jungle_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Jungle_Cabinet = register("jungle_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Jungle_Wardrobe = register("jungle_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Jungle_Stool = register("jungle_stool", new Chair(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final Block Jungle_Chair = register("jungle_chair", new Chair(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Jungle_Shutter = register("jungle_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final Block Jungle_Crate = register("jungle_crate", new Crate(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Shelf = register("jungle_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Potion_Shelf = register("jungle_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS)));

	public static final Block Spruce_Table = register("spruce_table", new Table(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Square_Table = register("spruce_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Bedside_Table = register("spruce_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Spruce_Kitchen_Cabinet = register("spruce_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Spruce_Cabinet = register("spruce_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Spruce_Wardrobe = register("spruce_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Spruce_Stool = register("spruce_stool", new Chair(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final Block Spruce_Chair = register("spruce_chair", new Chair(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final Block Spruce_Shutter = register("spruce_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final Block Spruce_Crate = register("spruce_crate", new Crate(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Shelf = register("spruce_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Potion_Shelf = register("spruce_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));

	public static final Block Dark_Oak_Table = register("dark_oak_table", new Table(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Square_Table = register("dark_oak_square_table", new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Bedside_Table = register("dark_oak_bedside_table", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Dark_Oak_Kitchen_Cabinet = register("dark_oak_kitchen_cabinet", new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Dark_Oak_Cabinet = register("dark_oak_cabinet", new Cabinet(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Dark_Oak_Wardrobe = register("dark_oak_wardrobe", new Wardrobe(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Dark_Oak_Stool = register("dark_oak_stool", new Chair(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final Block Dark_Oak_Chair = register("dark_oak_chair", new Chair(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final Block Dark_Oak_Shutter = register("dark_oak_shutter", new Shutter(AbstractBlock.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final Block Dark_Oak_Crate = register("dark_oak_crate", new Crate(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Shelf = register("dark_oak_shelf", new Shelf(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Potion_Shelf = register("dark_oak_potion_shelf", new PotionShelf(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS)));

	public static final Block Red_Bunting = register("red_bunting", new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE)));
	public static final Block Yellow_Bunting = register("yellow_bunting", new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE)));
	public static final Block Green_Bunting = register("green_bunting", new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE)));

	public static final Block Metal_Mailbox = register("metal_mailbox", new Mailbox(AbstractBlock.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE)));

	public static final Block Brick_Chimney_Conduit = register("brick_chimney_conduit", new ChimneyConduit(AbstractBlock.Properties.copy(Blocks.BRICKS)));
	public static final Block Brick_Chimney_Cap = register("chimney_cap", new ChimneyCap(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final Block Cooking_Pot = register("cooking_pot", new CookingPot(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(1.0f)));

	// public static final Block Drum_Tom = register("drum_tom", new Drum(AbstractBlock.Properties.copy(Blocks.NOTE_BLOCK), FurnishData.Sounds.Drum_Tom));
	// public static final Block Drum_Snare = register("drum_snare", new Drum(AbstractBlock.Properties.copy(Blocks.NOTE_BLOCK), FurnishData.Sounds.Drum_Snare));
	// public static final Block Cymbal = register("cymbal", new Cymbal(AbstractBlock.Properties.of(Material.METAL).strength(0.8f)));

	public static final Block Disk_Rack = register("disk_rack", new DiskRack(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS)));

	public static Block[] FurnitureInvProvider = {
			Oak_Bedside_Table, Oak_Kitchen_Cabinet, Oak_Cabinet,
			Birch_Bedside_Table, Birch_Kitchen_Cabinet, Birch_Cabinet,
			Spruce_Bedside_Table, Spruce_Kitchen_Cabinet, Spruce_Cabinet,
			Jungle_Bedside_Table, Jungle_Kitchen_Cabinet, Jungle_Cabinet,
			Acacia_Bedside_Table, Acacia_Kitchen_Cabinet, Acacia_Cabinet,
			Dark_Oak_Bedside_Table, Dark_Oak_Kitchen_Cabinet, Dark_Oak_Cabinet
	};

	public static Block[] FurnitureLargeInvProvider = {
			Oak_Wardrobe,
			Birch_Wardrobe,
			Spruce_Wardrobe,
			Jungle_Wardrobe,
			Acacia_Wardrobe,
			Dark_Oak_Wardrobe
	};

	public static Block[] Shelves = {
			Oak_Shelf,
			Birch_Shelf,
			Spruce_Shelf,
			Jungle_Shelf,
			Acacia_Shelf,
			Dark_Oak_Shelf
	};

	public static Block[] Potion_Shelves = {
			Oak_Potion_Shelf,
			Birch_Potion_Shelf,
			Spruce_Potion_Shelf,
			Jungle_Potion_Shelf,
			Acacia_Potion_Shelf,
			Dark_Oak_Potion_Shelf
	};

	public static Block[] Disk_Racks = {Disk_Rack};
	public static Block[] Cooking_Pots = {Cooking_Pot};

	public static final String[] Rare_Plates_Names = {"chinese", "english"};

	public static HashMap<String, Block> Carpets_On_Stairs = new HashMap<>(16);
	public static HashMap<String, Block> Carpets_On_Trapdoors = new HashMap<>(16);

	public static Block[] Amphorae = new Block[17];
	public static Block[] Sofas = new Block[16];
	public static Block[] Awnings = new Block[16];
	public static Block[] Curtains = new Block[16];
	public static Block[] Plates = new Block[17 + Rare_Plates_Names.length];
	public static Block[] Showcases = new Block[16];

	public static void setup(IEventBus bus) {
		Amphorae[0] = register("amphora", new Amphora(AbstractBlock.Properties.copy(Blocks.TERRACOTTA)));
		Plates[0] = register("plate", new Plate(AbstractBlock.Properties.copy(Blocks.TERRACOTTA)));

		int index = 0;
		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, register(String.format("%s_carpet_on_stairs", color), new CarpetOnStairs(AbstractBlock.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, register(String.format("%s_carpet_on_trapdoor", color), new CarpetOnTrapdoor(AbstractBlock.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings[index] = register(String.format("%s_awning", color), new Awning(AbstractBlock.Properties.copy(coloredCarpet)));
			Curtains[index] = register(String.format("%s_curtain", color), new Curtain(AbstractBlock.Properties.copy(coloredCarpet)));

			Block coloredWool = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas[index] = register(String.format("%s_sofa", color), new Sofa(AbstractBlock.Properties.copy(coloredWool)));
			Showcases[index] = register(String.format("%s_showcase", color), new Showcase(AbstractBlock.Properties.copy(Blocks.GLASS)));

			Block coloredTerracotta = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae[index + 1] = register(String.format("%s_amphora", color), new Amphora(AbstractBlock.Properties.copy(coloredTerracotta)));
			Plates[index + 1] = register(String.format("%s_plate", color), new Plate(AbstractBlock.Properties.copy(coloredTerracotta)));
			index++;
		}

		int plateIndex = index;
		for(String s : Rare_Plates_Names) {
			Plates[plateIndex + 1] = register(String.format("rare_%s_plate", s), new Plate(AbstractBlock.Properties.copy(Blocks.TERRACOTTA)));
			plateIndex++;
		}

		Registry.register(bus);
	}

	public static void clientSetup() {
		RenderTypeLookup.setRenderLayer(Red_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Yellow_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Green_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Jungle_Shutter, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Acacia_Shutter, RenderType.translucent());
		for(Block b : Showcases) RenderTypeLookup.setRenderLayer(b, RenderType.translucent());
		for(Block b : Potion_Shelves) RenderTypeLookup.setRenderLayer(b, RenderType.translucent());
	}

	private static Block register(String registryName, Block block) {
		Registry.register(registryName, () -> block);
		return block;
	}
}
