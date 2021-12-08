package io.github.wouink.furnish;

import io.github.wouink.furnish.block.*;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.block.tileentity.AmphoraTileEntity;
import io.github.wouink.furnish.block.tileentity.FurnitureTileEntity;
import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.event.*;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.recipe.FSingleItemRecipe;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Objects;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishManager {
	public static final Logger Furnish_Logger = LogManager.getLogger();

	public static final VoxelShape[] CHAIR_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 17, 13));
	public static final VoxelShape[] CHAIR_TALL_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 22, 13));
	public static final VoxelShape[] CHAIR_SEAT_THRONE = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 30, 13));

	public static final Block Furniture_Workbench = new FurnitureWorkbench();
	public static final Block Book_Pile = new BookPile(AbstractBlock.Properties.of(Material.WOOL).strength(0.2f), "book_pile");

	public static final Block Oak_Table = new Table(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_table");
	public static final Block Oak_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_square_table");
	public static final Block Oak_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_bedside_table", Sounds.Drawers_Open);
	public static final Block Oak_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Oak_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_cabinet", Sounds.Cabinet_Open);
	public static final Block Oak_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_wardrobe", Sounds.Cabinet_Open);
	public static final Block Oak_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_stool", Chair.BASE_SHAPES);
	public static final Block Oak_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_SEAT));
	public static final Block Oak_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.OAK_TRAPDOOR), "oak_shutter");

	public static final Block Birch_Table = new Table(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_table");
	public static final Block Birch_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_square_table");
	public static final Block Birch_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_cabinet", Sounds.Cabinet_Open);
	public static final Block Birch_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_wardrobe", Sounds.Cabinet_Open);
	public static final Block Birch_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Birch_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_bedside_table", Sounds.Drawers_Open);
	public static final Block Birch_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_SEAT));
	public static final Block Birch_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_stool", Chair.BASE_SHAPES);
	public static final Block Birch_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.BIRCH_PLANKS), "birch_shutter");

	public static final Block Acacia_Table = new Table(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_table");
	public static final Block Acacia_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_square_table");
	public static final Block Acacia_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_cabinet", Sounds.Cabinet_Open);
	public static final Block Acacia_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_wardrobe", Sounds.Cabinet_Open);
	public static final Block Acacia_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Acacia_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_bedside_table", Sounds.Drawers_Open);
	public static final Block Acacia_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_TALL_SEAT));
	public static final Block Acacia_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_stool", Chair.BASE_SHAPES);
	public static final Block Acacia_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.ACACIA_PLANKS), "acacia_shutter");

	public static final Block Jungle_Table = new Table(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_table");
	public static final Block Jungle_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_square_table");
	public static final Block Jungle_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_cabinet", Sounds.Cabinet_Open);
	public static final Block Jungle_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_wardrobe", Sounds.Cabinet_Open);
	public static final Block Jungle_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Jungle_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_bedside_table", Sounds.Drawers_Open);
	public static final Block Jungle_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_SEAT));
	public static final Block Jungle_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_stool", Chair.BASE_SHAPES);
	public static final Block Jungle_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.JUNGLE_PLANKS), "jungle_shutter");

	public static final Block Dark_Oak_Table = new Table(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_table");
	public static final Block Dark_Oak_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_square_table");
	public static final Block Dark_Oak_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_cabinet", Sounds.Spruce_Cabinet_Open);
	public static final Block Dark_Oak_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_wardrobe", Sounds.Spruce_Cabinet_Open);
	public static final Block Dark_Oak_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_bedside_table", Sounds.Drawers_Open);
	public static final Block Dark_Oak_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Dark_Oak_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_SEAT_THRONE));
	public static final Block Dark_Oak_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_stool", Chair.BASE_SHAPES);
	public static final Block Dark_Oak_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.DARK_OAK_PLANKS), "dark_oak_shutter");

	public static final Block Spruce_Table = new Table(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_table");
	public static final Block Spruce_Square_Table = new SimpleFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_square_table");
	public static final Block Spruce_Cabinet = new Cabinet(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_cabinet", Sounds.Spruce_Cabinet_Open);
	public static final Block Spruce_Wardrobe = new Wardrobe(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_wardrobe", Sounds.Spruce_Cabinet_Open);
	public static final Block Spruce_Kitchen_Cabinet = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_kitchen_cabinet", Sounds.Drawers_Open);
	public static final Block Spruce_Bedside_Table = new InventoryFurniture(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_bedside_table", Sounds.Drawers_Open);
	public static final Block Spruce_Chair = new Chair(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_chair", VoxelShapeHelper.getMergedShapes(Chair.BASE_SHAPES, CHAIR_TALL_SEAT));
	public static final Block Spruce_Stool = new Chair(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_stool", Chair.BASE_SHAPES);
	public static final Block Spruce_Shutter = new Shutter(AbstractBlock.Properties.copy(Blocks.SPRUCE_PLANKS), "spruce_shutter");

	public static final Block Red_Bunting = new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE), "red_bunting");
	public static final Block Yellow_Bunting = new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE), "yellow_bunting");
	public static final Block Green_Bunting = new Bunting(AbstractBlock.Properties.copy(Blocks.TRIPWIRE), "green_bunting");

	public static final Block Metal_Mailbox = new Mailbox(AbstractBlock.Properties.of(Material.METAL).noOcclusion().strength(2.0f).sound(SoundType.METAL).harvestTool(ToolType.PICKAXE), "metal_mailbox");

	public static final Block Brick_Chimney_Conduit = new ChimneyConduit(AbstractBlock.Properties.copy(Blocks.BRICKS), "brick_chimney_conduit");
	public static final Block Brick_Chimney_Cap = new ChimneyCap(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.LANTERN).strength(2.0f), "chimney_cap");

	// public static final Block Oak_Sideboard = new WideInventoryFurniture(AbstractBlock.Properties.copy(Blocks.OAK_PLANKS), "oak_sideboard");

	public static Block[] Mailboxes = {
		Metal_Mailbox
	};

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

	public static HashMap<String, Block> Carpets_On_Stairs = new HashMap<>(16);
	public static HashMap<String, Block> Carpets_On_Trapdoors = new HashMap<>(16);

	public static Block[] Amphorae = new Block[17];
	public static Block[] Sofas = new Block[16];
	public static Block[] Awnings = new Block[16];

	// called by Furnish @Mod class constructor
	public static void init() {
		Amphorae[0] = new Amphora(AbstractBlock.Properties.copy(Blocks.TERRACOTTA), "amphora");
		int index = 0;
		for(DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			Block coloredCarpet = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_carpet", color)));
			Carpets_On_Stairs.put(color, new CarpetOnStairs(AbstractBlock.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), String.format("%s_carpet_on_stairs", color), coloredCarpet));
			Carpets_On_Trapdoors.put(color, new CarpetOnTrapdoor(AbstractBlock.Properties.copy(coloredCarpet).dropsLike(coloredCarpet), String.format("%s_carpet_on_trapdoor", color), coloredCarpet));
			Awnings[index] = new Awning(AbstractBlock.Properties.copy(coloredCarpet), String.format("%s_awning", color));

			Block coloredWool = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_wool", color)));
			Sofas[index] = new Sofa(AbstractBlock.Properties.copy(coloredWool), String.format("%s_sofa", color));

			Block coloredTerracotta = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(String.format("minecraft:%s_terracotta", color)));
			Amphorae[index + 1] = new Amphora(AbstractBlock.Properties.copy(coloredTerracotta), String.format("%s_amphora", color));
			index++;
		}
		MinecraftForge.EVENT_BUS.register(new PlaceCarpet());
		MinecraftForge.EVENT_BUS.register(new AddArmsToArmorStand());
		MinecraftForge.EVENT_BUS.register(new CyclePainting());
		MinecraftForge.EVENT_BUS.register(new KnockOnDoor());
	}

	// called by Furnish @Mod class clientSetup
	public static void registerTransparency() {
		RenderTypeLookup.setRenderLayer(Red_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Yellow_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Green_Bunting, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Jungle_Shutter, RenderType.translucent());
		RenderTypeLookup.setRenderLayer(Acacia_Shutter, RenderType.translucent());
	}

	public static class ModBlocks {
		public static final DeferredRegister<Block> Blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, Furnish.MODID);
		public static void register(String name, Block b) {
			ModBlocks.Blocks.register(name, () -> b);
		}
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
		public static final RegistryObject<TileEntityType<AmphoraTileEntity>> Amphora = register("amphora", AmphoraTileEntity::new, () -> Amphorae);
		public static final RegistryObject<TileEntityType<MailboxTileEntity>> Mailbox = register("mailbox", MailboxTileEntity::new, () -> Mailboxes);

		private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<Block[]> validBlockSupplier) {
			return Furnish_Tile_Entities.register(name, () -> TileEntityType.Builder.of(factory, validBlockSupplier.get()).build(null));
		}
	}

	public static class Sounds {
		public static final DeferredRegister<SoundEvent> Furnish_Sounds = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Furnish.MODID);
		public static final RegistryObject<SoundEvent> Cabinet_Open = Furnish_Sounds.register("furniture.open", () -> register("furniture.open"));
		public static final RegistryObject<SoundEvent> Spruce_Cabinet_Open = Furnish_Sounds.register("furniture.open.spruce", () -> register("furniture.open.spruce"));
		public static final RegistryObject<SoundEvent> Drawers_Open = Furnish_Sounds.register("furniture.open.drawers", () -> register("furniture.open.drawers"));
		public static final RegistryObject<SoundEvent> Amphora_Open = Furnish_Sounds.register("amphora.open", () -> register("amphora.open"));
		public static final RegistryObject<SoundEvent> Wooden_Door_Knock = Furnish_Sounds.register("door.knock.wood", () -> register("door.knock.wood"));
		public static final RegistryObject<SoundEvent> Iron_Door_Knock = Furnish_Sounds.register("door.knock.iron", () -> register("door.knock.iron"));
		public static final RegistryObject<SoundEvent> Mailbox_Update = Furnish_Sounds.register("mailbox.update", () -> register("mailbox.update"));
		public static final RegistryObject<SoundEvent> Attach_To_Letter = Furnish_Sounds.register("letter.add_attachment", () -> register("letter.add_attachment"));
		public static final RegistryObject<SoundEvent> Detach_From_Letter = Furnish_Sounds.register("letter.remove_attachment", () -> register("letter.remove_attachment"));

		public static SoundEvent register(String name) {
			return new SoundEvent(new ResourceLocation(Furnish.MODID, name));
		}
	}

	public static final ItemGroup Furnish_ItemGroup = new ItemGroup(Furnish.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(Furniture_Workbench);
		}
	};

	public static BlockItem getBlockItem(Block block) {
		return (BlockItem) new BlockItem(block, new Item.Properties().tab(Furnish_ItemGroup)).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
		IForgeRegistry<Item> itemRegistry = itemRegistryEvent.getRegistry();
		for(RegistryObject<Block> b : ModBlocks.Blocks.getEntries()) {
			if(!(b.get() instanceof CarpetOnStairs) && !(b.get() instanceof CarpetOnTrapdoor)) {
				itemRegistry.register(getBlockItem(b.get()));
			}
		}
		itemRegistry.register(new Letter(new Item.Properties().tab(Furnish_ItemGroup).stacksTo(1), "letter"));
		Furnish_Logger.info("Registered Furnish Items.");
	}

	public static PaintingType createPainting(String name, int w, int h) {
		PaintingType painting = new PaintingType(16 * w, 16 * h);
		painting.setRegistryName(Furnish.MODID, name);
		return painting;
	}

	@SubscribeEvent
	public static void registerPaintings(RegistryEvent.Register<PaintingType> paintingRegistryEvent) {
		IForgeRegistry<PaintingType> paintingRegistry = paintingRegistryEvent.getRegistry();

		// register Furnish paintings
		paintingRegistry.register(createPainting("steve", 1, 1));
		paintingRegistry.register(createPainting("alex", 1, 1));

		// String paintingsJsonFile = "config/furnish_custom_paintings.json";
		// CustomPaintings.registerCustomPaintings(paintingRegistry, paintingsJsonFile);
	}
}
