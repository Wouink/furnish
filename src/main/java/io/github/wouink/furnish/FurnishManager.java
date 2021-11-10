package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.block.tileentity.FurnitureTileEntity;
import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
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

	public static final Block Furniture_Workbench = new FurnitureWorkbench();
	public static final Block Oak_Square_Table = new TableBlock(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_square_table");
	public static final Block Oak_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_bedside_table");
	public static final Block Oak_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_kitchen_cabinet");
	public static final Block Book_Pile = new BookPile(AbstractBlock.Properties.of(Material.WOOL).strength(0.5f), "book_pile");
	public static final Block Oak_Chair = new ChairBlock(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_chair");
	public static final Block Oak_Wardrobe = new TallInventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_wardrobe");
	public static final Block Oak_Sideboard = new WideInventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_sideboard");

	public static Block[] FurnitureInvProvider = {
			Oak_Bedside_Table,
			Oak_Kitchen_Cabinet
	};

	public static Block[] FurnitureLargeInvProvider = {
			Oak_Wardrobe
	};

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
		public static DeferredRegister<TileEntityType<?>> Furnish_Tile_Entities = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Furnish.MODID);
		public static final RegistryObject<TileEntityType<FurnitureTileEntity>> Furniture = register("furniture", FurnitureTileEntity::new, () -> FurnitureInvProvider);
		public static final RegistryObject<TileEntityType<LargeFurnitureTileEntity>> Large_Furniture = register("large_furniture", LargeFurnitureTileEntity::new, () -> FurnitureLargeInvProvider);

		private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<Block[]> validBlockSupplier) {
			return Furnish_Tile_Entities.register(name, () -> TileEntityType.Builder.of(factory, validBlockSupplier.get()).build(null));
		}
	}

	@SubscribeEvent
	public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
		IForgeRegistry<Block> blockRegistry = blockRegistryEvent.getRegistry();
		blockRegistry.register(Furniture_Workbench);
		blockRegistry.register(Oak_Square_Table);
		blockRegistry.register(Oak_Bedside_Table);
		blockRegistry.register(Oak_Kitchen_Cabinet);
		blockRegistry.register(Book_Pile);
		blockRegistry.register(Oak_Chair);
		blockRegistry.register(Oak_Wardrobe);
		blockRegistry.register(Oak_Sideboard);
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
		itemRegistry.register(getBlockItem(Oak_Square_Table));
		itemRegistry.register(getBlockItem(Oak_Bedside_Table));
		itemRegistry.register(getBlockItem(Oak_Kitchen_Cabinet));
		itemRegistry.register(getBlockItem(Book_Pile));
		itemRegistry.register(getBlockItem(Oak_Chair));
		itemRegistry.register(getBlockItem(Oak_Wardrobe));
		itemRegistry.register(getBlockItem(Oak_Sideboard));
	}
}
