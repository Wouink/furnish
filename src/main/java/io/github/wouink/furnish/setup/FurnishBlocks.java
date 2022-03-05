package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

public class FurnishBlocks {
	public static final DeferredRegister<Block> Registry = DeferredRegister.create(ForgeRegistries.BLOCKS, Furnish.MODID);

	public static class CustomProperties {
		public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	}

	public static final Block Furniture_Workbench = register("furniture_workbench", new FurnitureWorkbench());
	public static final Block Book_Pile = register("book_pile", new BookPile(BlockBehaviour.Properties.of(Material.WOOL).strength(0.2f)));

	public static final Block Oak_Table = register("oak_table", new Table(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Square_Table = register("oak_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Pedestal_Table = register("oak_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Bedside_Table = register("oak_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Oak_Kitchen_Cabinet = register("oak_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Oak_Cabinet = register("oak_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Oak_Wardrobe = register("oak_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Oak_Stool = register("oak_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), Chair.BASE_SHAPES));
	public static final Block Oak_Chair = register("oak_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Oak_Shutter = register("oak_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR)));
	public static final Block Oak_Crate = register("oak_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
	public static final Block Oak_Shelf = register("oak_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));

	public static final Block Birch_Table = register("birch_table", new Table(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Square_Table = register("birch_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Pedestal_Table = register("birch_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Bedside_Table = register("birch_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Birch_Kitchen_Cabinet = register("birch_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Birch_Cabinet = register("birch_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Birch_Wardrobe = register("birch_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Birch_Stool = register("birch_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), Chair.BASE_SHAPES));
	public static final Block Birch_Chair = register("birch_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Birch_Shutter = register("birch_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.BIRCH_TRAPDOOR)));
	public static final Block Birch_Crate = register("birch_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));
	public static final Block Birch_Shelf = register("birch_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS)));

	public static final Block Acacia_Table = register("acacia_table", new Table(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Square_Table = register("acacia_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Pedestal_Table = register("acacia_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Bedside_Table = register("acacia_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Acacia_Kitchen_Cabinet = register("acacia_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Acacia_Cabinet = register("acacia_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Acacia_Wardrobe = register("acacia_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Acacia_Stool = register("acacia_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), Chair.BASE_SHAPES));
	public static final Block Acacia_Chair = register("acacia_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final Block Acacia_Shutter = register("acacia_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.ACACIA_TRAPDOOR)));
	public static final Block Acacia_Crate = register("acacia_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));
	public static final Block Acacia_Shelf = register("acacia_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS)));

	public static final Block Jungle_Table = register("jungle_table", new Table(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Square_Table = register("jungle_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Pedestal_Table = register("jungle_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Bedside_Table = register("jungle_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Jungle_Kitchen_Cabinet = register("jungle_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Jungle_Cabinet = register("jungle_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Jungle_Wardrobe = register("jungle_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), FurnishData.Sounds.Cabinet_Open, FurnishData.Sounds.Cabinet_Close));
	public static final Block Jungle_Stool = register("jungle_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), Chair.BASE_SHAPES));
	public static final Block Jungle_Chair = register("jungle_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT)));
	public static final Block Jungle_Shutter = register("jungle_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.JUNGLE_TRAPDOOR)));
	public static final Block Jungle_Crate = register("jungle_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));
	public static final Block Jungle_Shelf = register("jungle_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS)));

	public static final Block Spruce_Table = register("spruce_table", new Table(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Square_Table = register("spruce_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Pedestal_Table = register("spruce_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Bedside_Table = register("spruce_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Spruce_Kitchen_Cabinet = register("spruce_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Spruce_Cabinet = register("spruce_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Spruce_Wardrobe = register("spruce_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Spruce_Stool = register("spruce_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), Chair.BASE_SHAPES));
	public static final Block Spruce_Chair = register("spruce_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_TALL_SEAT)));
	public static final Block Spruce_Shutter = register("spruce_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.SPRUCE_TRAPDOOR)));
	public static final Block Spruce_Crate = register("spruce_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));
	public static final Block Spruce_Shelf = register("spruce_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	public static final Block Dark_Oak_Table = register("dark_oak_table", new Table(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Square_Table = register("dark_oak_square_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Pedestal_Table = register("dark_oak_pedestal_table", new SimpleFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Bedside_Table = register("dark_oak_bedside_table", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Dark_Oak_Kitchen_Cabinet = register("dark_oak_kitchen_cabinet", new InventoryFurniture(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Drawers_Open, FurnishData.Sounds.Drawers_Close));
	public static final Block Dark_Oak_Cabinet = register("dark_oak_cabinet", new Cabinet(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Dark_Oak_Wardrobe = register("dark_oak_wardrobe", new Wardrobe(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), FurnishData.Sounds.Spruce_Cabinet_Open, FurnishData.Sounds.Spruce_Cabinet_Close));
	public static final Block Dark_Oak_Stool = register("dark_oak_stool", new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), Chair.BASE_SHAPES));
	public static final Block Dark_Oak_Chair = register("dark_oak_chair", new Chair(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS), VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, Chair.CHAIR_SEAT_THRONE)));
	public static final Block Dark_Oak_Shutter = register("dark_oak_shutter", new Shutter(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_TRAPDOOR)));
	public static final Block Dark_Oak_Crate = register("dark_oak_crate", new Crate(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));
	public static final Block Dark_Oak_Shelf = register("dark_oak_shelf", new Shelf(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS)));

	public static final Block Red_Bunting = register("red_bunting", new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final Block Yellow_Bunting = register("yellow_bunting", new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final Block Green_Bunting = register("green_bunting", new Bunting(BlockBehaviour.Properties.copy(Blocks.TRIPWIRE)));
	public static final Block Lantern_Bunting = register("lantern_bunting", new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 15)));
	public static final Block Soul_Lantern_Bunting = register("soul_lantern_bunting", new LanternBunting(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.LANTERN).lightLevel((state) -> 10)));

	public static final Block Metal_Mailbox = register("metal_mailbox", new Mailbox(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL)));

	public static final Block Brick_Chimney_Conduit = register("brick_chimney_conduit", new ChimneyConduit(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(1.0f, 3.0f)));
	public static final Block Brick_Chimney_Cap = register("chimney_cap", new ChimneyCap(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f)));

	public static final Block Disk_Rack = register("disk_rack", new DiskRack(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS)));

	public static final Block Paper_Lamp = register("paper_lamp", new PaperLamp());
	public static final Block Red_Paper_Lamp = register("red_paper_lamp", new PaperLamp());

	public static final Block Heavy_Metal = register("heavy_metal", new Block(BlockBehaviour.Properties.of(Material.METAL).sound(SoundType.METAL).requiresCorrectToolForDrops().strength(4.0f, 8.0f)));

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

	public static Block[] Disk_Racks = {Disk_Rack};
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
		Amphorae[0] = register("amphora", new Amphora(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)));
		Plates[0] = register("plate", new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)));

		int index = 0;
		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, register(String.format("%s_carpet_on_stairs", color), new CarpetOnStairs(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Carpets_On_Trapdoors.put(color, register(String.format("%s_carpet_on_trapdoor", color), new CarpetOnTrapdoor(BlockBehaviour.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), coloredCarpet)));
			Awnings[index] = register(String.format("%s_awning", color), new Awning(BlockBehaviour.Properties.copy(coloredCarpet)));
			Curtains[index] = register(String.format("%s_curtain", color), new Curtain(BlockBehaviour.Properties.copy(coloredCarpet)));

			Block coloredWool = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas[index] = register(String.format("%s_sofa", color), new Sofa(BlockBehaviour.Properties.copy(coloredWool)));
			Showcases[index] = register(String.format("%s_showcase", color), new Showcase(BlockBehaviour.Properties.copy(Blocks.GLASS)));

			Block coloredTerracotta = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae[index + 1] = register(String.format("%s_amphora", color), new Amphora(BlockBehaviour.Properties.copy(coloredTerracotta)));
			Plates[index + 1] = register(String.format("%s_plate", color), new Plate(BlockBehaviour.Properties.copy(coloredTerracotta)));
			index++;
		}

		int plateIndex = index;
		for(String s : Rare_Plates_Names) {
			Plates[plateIndex + 1] = register(String.format("rare_%s_plate", s), new Plate(BlockBehaviour.Properties.copy(Blocks.TERRACOTTA)));
			plateIndex++;
		}

		Registry.register(bus);
	}

	public static void clientSetup() {
		ItemBlockRenderTypes.setRenderLayer(Red_Bunting, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Yellow_Bunting, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Green_Bunting, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Jungle_Shutter, RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(Acacia_Shutter, RenderType.translucent());
		for(Block b : Showcases) ItemBlockRenderTypes.setRenderLayer(b, RenderType.translucent());
	}

	private static Block register(String registryName, Block block) {
		Registry.register(registryName, () -> block);
		return block;
	}
}
