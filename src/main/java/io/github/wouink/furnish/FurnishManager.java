package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.block.tileentity.FurnitureTileEntity;
import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.recipe.FSingleItemRecipe;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishManager {
	public static final Logger Furnish_Logger = LogManager.getLogger();

	public static final VoxelShape[] CHAIR_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 17, 13));
	public static final VoxelShape[] CHAIR_TALL_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 22, 13));

	public static final Block Furniture_Workbench = new FurnitureWorkbench();
	public static final Block Book_Pile = new BookPile(AbstractBlock.Properties.of(Material.WOOL).strength(0.2f), "book_pile");

	public static final Block Oak_Table = new Table(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_table");
	public static final Block Oak_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_square_table");
	public static final Block Oak_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_bedside_table");
	public static final Block Oak_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_kitchen_cabinet");
	public static final Block Oak_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_cabinet");
	public static final Block Oak_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_wardrobe");
	public static final Block Oak_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_stool", Chair.BASE_SHAPES);
	public static final Block Oak_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_SEAT));
	public static final Block Oak_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR), "oak_shutter");

	public static final Block Birch_Table = new Table(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_table");
	public static final Block Birch_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_square_table");
	public static final Block Birch_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_cabinet");
	public static final Block Birch_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_wardrobe");
	public static final Block Birch_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_kitchen_cabinet");
	public static final Block Birch_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_bedside_table");

	public static final Block Acacia_Table = new Table(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_table");
	public static final Block Acacia_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_square_table");
	public static final Block Acacia_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_cabinet");
	public static final Block Acacia_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_wardrobe");
	public static final Block Acacia_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_kitchen_cabinet");
	public static final Block Acacia_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_bedside_table");

	public static final Block Jungle_Table = new Table(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_table");
	public static final Block Jungle_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_square_table");
	public static final Block Jungle_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_cabinet");
	public static final Block Jungle_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_wardrobe");
	public static final Block Jungle_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_kitchen_cabinet");
	public static final Block Jungle_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_bedside_table");

	public static final Block Dark_Oak_Table = new Table(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_table");
	public static final Block Dark_Oak_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_square_table");
	public static final Block Dark_Oak_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_cabinet");
	public static final Block Dark_Oak_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_wardrobe");
	public static final Block Dark_Oak_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_bedside_table");
	public static final Block Dark_Oak_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_kitchen_cabinet");
	public static final Block Dark_Oak_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_TALL_SEAT));

	public static final Block Spruce_Table = new Table(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_table");
	public static final Block Spruce_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_square_table");
	public static final Block Spruce_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_cabinet");
	public static final Block Spruce_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_wardrobe");
	public static final Block Spruce_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_kitchen_cabinet");
	public static final Block Spruce_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_bedside_table");

	// public static final Block Oak_Sideboard = new WideInventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_sideboard");

	public static Block[] FurnitureInvProvider = {
			Oak_Bedside_Table,
			Oak_Kitchen_Cabinet,
			Oak_Cabinet
	};

	public static Block[] FurnitureLargeInvProvider = {
			Oak_Wardrobe,
	};

	public static Block[] Amphoras = new Block[1];
	public static final String[] Colors = {
			"black", "red", "green", "brown", "blue", "purple", "cyan", "light_gray", "gray", "pink", "lime", "yellow", "light_blue", "magenta", "orange", "white"
	};

	public static void init() {
		Amphoras[0] = new Amphora(AbstractBlock.Properties.copy(Blocks.TERRACOTTA), "amphora");
	}

	public static class Serializer {
		public static final DeferredRegister<IRecipeSerializer<?>> Recipe_Serializers = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Furnish.MODID);
		public static final RegistryObject<IRecipeSerializer<FurnitureRecipe>> Furniture = Recipe_Serializers.register(
				"furniture_making",
				() -> new FSingleItemRecipe.Serializer<FurnitureRecipe>(FurnitureRecipe::new) {}
		);
	}

	public static class RecipeType {
		public static final IRecipeType<FurnitureRecipe> Furniture_Recipe = IRecipeType.register(Furnish.MODID + ":furniture_making");
	}

	public static class Containers {
		public static final DeferredRegister<ContainerType<?>> Container_Types = DeferredRegister.create(ForgeRegistries.CONTAINERS, Furnish.MODID);
		public static final RegistryObject<ContainerType<FurnitureWorkbenchContainer>> Furniture_Workbench = Container_Types.register("furniture_workbench",
				() -> new ContainerType<>(FurnitureWorkbenchContainer::new)
		);
	}

	public static class Entities {
		public static final DeferredRegister<EntityType<?>> Furnish_Entities = DeferredRegister.create(ForgeRegistries.ENTITIES, Furnish.MODID);
		public static final RegistryObject<EntityType<SeatEntity>> Seat_Entity = register("seat",
				EntityType.Builder.<SeatEntity>of((type, world) -> new SeatEntity(world), EntityClassification.MISC)
						.sized(0.0f, 0.0f).setCustomClientFactory(((spawnEntity, world) -> new SeatEntity(world)))
		);

		private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
			return Furnish_Entities.register(name, () -> builder.build(name));
		}
	}

	public static class TileEntities {
		public static final DeferredRegister<TileEntityType<?>> Furnish_Tile_Entities = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Furnish.MODID);
		public static final RegistryObject<TileEntityType<FurnitureTileEntity>> Furniture = register("furniture", FurnitureTileEntity::new, () -> FurnitureInvProvider);
		public static final RegistryObject<TileEntityType<LargeFurnitureTileEntity>> Large_Furniture = register("large_furniture", LargeFurnitureTileEntity::new, () -> FurnitureLargeInvProvider);
		public static final RegistryObject<TileEntityType<FurnitureTileEntity>> Amphora = register("amphora", FurnitureTileEntity::new, () -> Amphoras);

		private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<Block[]> validBlockSupplier) {
			return Furnish_Tile_Entities.register(name, () -> TileEntityType.Builder.of(factory, validBlockSupplier.get()).build(null));
		}
	}

	public static class Sounds {
		public static final DeferredRegister<SoundEvent> Furnish_Sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Furnish.MODID);
		public static final RegistryObject<SoundEvent> Open_Furniture = Furnish_Sounds.register("furniture.open", () -> register("furniture.open"));

		public static SoundEvent register(String name) {
			return new SoundEvent(new ResourceLocation(Furnish.MODID, name));
		}
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		IForgeRegistry<Block> blockRegistry = blockRegistryEvent.getRegistry();
		blockRegistry.register(Furniture_Workbench);
		blockRegistry.register(Book_Pile);

		blockRegistry.register(Oak_Table);
		blockRegistry.register(Oak_Square_Table);
		blockRegistry.register(Oak_Bedside_Table);
		blockRegistry.register(Oak_Kitchen_Cabinet);
		blockRegistry.register(Oak_Cabinet);
		blockRegistry.register(Oak_Wardrobe);
		blockRegistry.register(Oak_Stool);
		blockRegistry.register(Oak_Chair);
		blockRegistry.register(Oak_Shutter);

		blockRegistry.register(Birch_Table);
		blockRegistry.register(Birch_Square_Table);
		blockRegistry.register(Birch_Cabinet);
		blockRegistry.register(Birch_Wardrobe);
		blockRegistry.register(Birch_Kitchen_Cabinet);
		blockRegistry.register(Birch_Bedside_Table);

		blockRegistry.register(Acacia_Table);
		blockRegistry.register(Acacia_Square_Table);
		blockRegistry.register(Acacia_Cabinet);
		blockRegistry.register(Acacia_Wardrobe);
		blockRegistry.register(Acacia_Kitchen_Cabinet);
		blockRegistry.register(Acacia_Bedside_Table);

		blockRegistry.register(Jungle_Table);
		blockRegistry.register(Jungle_Square_Table);
		blockRegistry.register(Jungle_Cabinet);
		blockRegistry.register(Jungle_Wardrobe);
		blockRegistry.register(Jungle_Kitchen_Cabinet);
		blockRegistry.register(Jungle_Bedside_Table);

		blockRegistry.register(Dark_Oak_Table);
		blockRegistry.register(Dark_Oak_Square_Table);
		blockRegistry.register(Dark_Oak_Cabinet);
		blockRegistry.register(Dark_Oak_Wardrobe);
		blockRegistry.register(Dark_Oak_Bedside_Table);
		blockRegistry.register(Dark_Oak_Kitchen_Cabinet);
		blockRegistry.register(Dark_Oak_Chair);

		blockRegistry.register(Spruce_Table);
		blockRegistry.register(Spruce_Square_Table);
		blockRegistry.register(Spruce_Cabinet);
		blockRegistry.register(Spruce_Wardrobe);
		blockRegistry.register(Spruce_Kitchen_Cabinet);
		blockRegistry.register(Spruce_Bedside_Table);

		for(Block b : Amphoras) {
			blockRegistry.register(b);
		}
		Furnish_Logger.info("Registered Furnish Blocks.");
	}

	private static BlockItem getBlockItem(Block block) {
		return (BlockItem) new BlockItem(block, new Item.Properties()).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	private static BlockItem getBlockItem(Block block, ItemGroup group) {
		return (BlockItem) new BlockItem(block, new Item.Properties().tab(group)).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
		IForgeRegistry<Item> itemRegistry = itemRegistryEvent.getRegistry();
		itemRegistry.register(getBlockItem(Furniture_Workbench, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Book_Pile, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Oak_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Bedside_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Stool, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Chair, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Oak_Shutter, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Birch_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Birch_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Birch_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Birch_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Birch_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Birch_Bedside_Table, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Acacia_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Acacia_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Acacia_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Acacia_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Acacia_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Acacia_Bedside_Table, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Jungle_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Jungle_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Jungle_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Jungle_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Jungle_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Jungle_Bedside_Table, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Dark_Oak_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Bedside_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Dark_Oak_Chair, ItemGroup.TAB_DECORATIONS));

		itemRegistry.register(getBlockItem(Spruce_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Spruce_Square_Table, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Spruce_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Spruce_Wardrobe, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Spruce_Kitchen_Cabinet, ItemGroup.TAB_DECORATIONS));
		itemRegistry.register(getBlockItem(Spruce_Bedside_Table, ItemGroup.TAB_DECORATIONS));

		for(Block b : Amphoras) {
			itemRegistry.register(getBlockItem(b, ItemGroup.TAB_DECORATIONS));
		}
		Furnish_Logger.info("Registered Furnish Items.");
	}
}
