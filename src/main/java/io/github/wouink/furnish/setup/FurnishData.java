package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.Crate;
import io.github.wouink.furnish.block.Mailbox;
import io.github.wouink.furnish.block.container.CrateContainer;
import io.github.wouink.furnish.block.container.DiskRackContainer;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.block.container.MailboxContainer;
import io.github.wouink.furnish.block.tileentity.*;
import io.github.wouink.furnish.client.gui.ConditionalSlotContainerScreen;
import io.github.wouink.furnish.client.gui.DiskRackScreen;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.renderer.*;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.recipe.FSingleItemRecipe;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishData {
	public static final ResourceLocation Furniture_Recipe_Loc = new ResourceLocation(Furnish.MODID, "furniture_making");
	public static RecipeType<FurnitureRecipe> Furniture_Recipe;

	@SubscribeEvent
	public static void registerRecipeType(RegistryEvent.Register<Block> event) {
		// Forge does not include a registry for RecipeTypes, and starting from 1.18.2,
		// registering in a vanilla registry must be done in any registry event.
		Furniture_Recipe = RecipeType.register(Furniture_Recipe_Loc.toString());
		System.out.println("Registered Furnish Recipe Type.");
	}

	public static class RecipeSerializers {
		public static final DeferredRegister<RecipeSerializer<?>> Registry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Furnish.MODID);
		public static final RegistryObject<RecipeSerializer<FurnitureRecipe>> Furniture_Recipe_Serializer = Registry.register(
				"furniture_making",
				() -> new FSingleItemRecipe.Serializer<FurnitureRecipe>(FurnitureRecipe::new) {}
		);
	}

	public static class Sounds {
		public static final DeferredRegister<SoundEvent> Registry = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Furnish.MODID);
		public static final RegistryObject<SoundEvent> Cabinet_Open = registerSound("block.furniture.open");
		public static final RegistryObject<SoundEvent> Cabinet_Close = registerSound("block.furniture.close");
		public static final RegistryObject<SoundEvent> Spruce_Cabinet_Open = registerSound("block.furniture_spruce.open");
		public static final RegistryObject<SoundEvent> Spruce_Cabinet_Close = registerSound("block.furniture_spruce.close");
		public static final RegistryObject<SoundEvent> Drawers_Open = registerSound("block.furniture_drawers.open");
		public static final RegistryObject<SoundEvent> Drawers_Close = registerSound("block.furniture_drawers.close");
		public static final RegistryObject<SoundEvent> Locker_Open = registerSound("block.furniture_locker.open");
		public static final RegistryObject<SoundEvent> Locker_Close = registerSound("block.furniture_locker.close");
		public static final RegistryObject<SoundEvent> Amphora_Open = registerSound("block.amphora.open");
		public static final RegistryObject<SoundEvent> Amphora_Close = registerSound("block.amphora.close");
		public static final RegistryObject<SoundEvent> Wooden_Door_Knock = registerSound("event.knock_on_door.wood");
		public static final RegistryObject<SoundEvent> Iron_Door_Knock = registerSound("event.knock_on_door.iron");
		public static final RegistryObject<SoundEvent> Mailbox_Update = registerSound("block.mailbox.update");
		public static final RegistryObject<SoundEvent> Attach_To_Letter = registerSound("item.letter.add_attachment");
		public static final RegistryObject<SoundEvent> Detach_From_Letter = registerSound("item.letter.remove_attachment");
		public static final RegistryObject<SoundEvent> Curtain = registerSound("block.curtain.interact");

		private static RegistryObject<SoundEvent> registerSound(String key) {
			return Registry.register(key, () -> new SoundEvent(new ResourceLocation(Furnish.MODID, key)));
		}
	}

	public static class Containers {
		public static final DeferredRegister<MenuType<?>> Registry = DeferredRegister.create(ForgeRegistries.CONTAINERS, Furnish.MODID);
		public static final RegistryObject<MenuType<FurnitureWorkbenchContainer>> Furniture_Workbench = Registry.register(
				"furniture_workbench",
				() -> new MenuType<>(FurnitureWorkbenchContainer::new)
		);
		public static final RegistryObject<MenuType<CrateContainer>> Crate = Registry.register(
				"crate",
				() -> new MenuType<>(CrateContainer::new)
		);
		public static final RegistryObject<MenuType<MailboxContainer>> Mailbox = Registry.register(
				"mailbox",
				() -> new MenuType<>(MailboxContainer::new)
		);
		public static final RegistryObject<MenuType<DiskRackContainer>> Disk_Rack = Registry.register(
				"disk_rack",
				() -> new MenuType<>(DiskRackContainer::new)
		);
	}

	public static class Entities {
		public static final DeferredRegister<EntityType<?>> Registry = DeferredRegister.create(ForgeRegistries.ENTITIES, Furnish.MODID);
		public static final RegistryObject<EntityType<SeatEntity>> Seat_Entity = register("seat",
				EntityType.Builder.<SeatEntity>of((type, world) -> new SeatEntity(world), MobCategory.MISC)
						.sized(0.0f, 0.0f).setCustomClientFactory(((spawnEntity, world) -> new SeatEntity(world)))
		);

		private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
			return Registry.register(name, () -> builder.build(name));
		}
	}

	public static class TileEntities {
		public static final DeferredRegister<BlockEntityType<?>> Registry = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Furnish.MODID);
		public static final RegistryObject<BlockEntityType<FurnitureTileEntity>> TE_Furniture = Registry.register("furniture", () -> BlockEntityType.Builder.of(FurnitureTileEntity::new, FurnishBlocks.Furniture_3x9.toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<LargeFurnitureTileEntity>> TE_Large_Furniture = Registry.register("large_furniture", () -> BlockEntityType.Builder.of(LargeFurnitureTileEntity::new, FurnishBlocks.Furniture_6x9.toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<AmphoraTileEntity>> TE_Amphora = Registry.register("amphora", () -> BlockEntityType.Builder.of(AmphoraTileEntity::new, FurnishBlocks.Amphorae.stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<MailboxTileEntity>> TE_Mailbox = Registry.register("mailbox", () -> BlockEntityType.Builder.of(MailboxTileEntity::new, Mailbox.All_Mailboxes.toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<CrateTileEntity>> TE_Crate = Registry.register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, Crate.All_Crates.toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<PlateTileEntity>> TE_Plate = Registry.register("plate", () -> BlockEntityType.Builder.of(PlateTileEntity::new, FurnishBlocks.Plates.stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<ShelfTileEntity>> TE_Shelf = Registry.register("shelf", () -> BlockEntityType.Builder.of(ShelfTileEntity::new, FurnishBlocks.Shelves.toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<ShowcaseTileEntity>> TE_Showcase = Registry.register("showcase", () -> BlockEntityType.Builder.of(ShowcaseTileEntity::new, FurnishBlocks.Showcases.stream().map(RegistryObject::get).toArray(Block[]::new)).build(null));
		public static final RegistryObject<BlockEntityType<DiskRackTileEntity>> TE_Disk_Rack = Registry.register("disk_rack", () -> BlockEntityType.Builder.of(DiskRackTileEntity::new, new Block[]{FurnishBlocks.Disk_Rack.get()}).build(null));
	}

	public static void setup(IEventBus bus) {
		Containers.Registry.register(bus);
		Furnish.LOG.info("Registered Furnish Containers.");
		RecipeSerializers.Registry.register(bus);
		Furnish.LOG.info("Registered Furnish Recipes Serializers.");
		Entities.Registry.register(bus);
		Furnish.LOG.info("Registered Furnish Entities.");
		TileEntities.Registry.register(bus);
		Furnish.LOG.info("Registered Furnish Tile Entities.");
		Sounds.Registry.register(bus);
		Furnish.LOG.info("Registered Furnish Sounds.");
	}

	public static void clientSetup() {
		MenuScreens.register(Containers.Furniture_Workbench.get(), FurnitureWorkbenchScreen::new);
		MenuScreens.register(Containers.Crate.get(), ConditionalSlotContainerScreen::new);
		MenuScreens.register(Containers.Mailbox.get(), ConditionalSlotContainerScreen::new);
		MenuScreens.register(Containers.Disk_Rack.get(), DiskRackScreen::new);
		Furnish.LOG.info("Registered Furnish Screens.");
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(Entities.Seat_Entity.get(), SeatRenderer::new);
		event.registerBlockEntityRenderer(TileEntities.TE_Mailbox.get(), MailboxRenderer::new);
		event.registerBlockEntityRenderer(TileEntities.TE_Plate.get(), PlateRenderer::new);
		event.registerBlockEntityRenderer(TileEntities.TE_Shelf.get(), ShelfRenderer::new);
		event.registerBlockEntityRenderer(TileEntities.TE_Showcase.get(), ShowcaseRenderer::new);
		event.registerBlockEntityRenderer(TileEntities.TE_Disk_Rack.get(), DiskRackRenderer::new);
		Furnish.LOG.info("Registered Furnish Entity/BlockEntity Renderers.");
	}

	public static Motive createPainting(String name, int w, int h) {
		Motive painting = new Motive(16 * w, 16 * h);
		painting.setRegistryName(Furnish.MODID, name);
		return painting;
	}

	@SubscribeEvent
	public static void registerPaintings(RegistryEvent.Register<Motive> paintingRegistryEvent) {
		IForgeRegistry<Motive> paintingRegistry = paintingRegistryEvent.getRegistry();

		paintingRegistry.register(createPainting("steve", 1, 1));
		paintingRegistry.register(createPainting("alex", 1, 1));
		paintingRegistry.register(createPainting("oak", 1, 1));
		paintingRegistry.register(createPainting("birch", 1, 1));
		paintingRegistry.register(createPainting("spruce", 1, 2));
		paintingRegistry.register(createPainting("jungle", 1, 2));
		paintingRegistry.register(createPainting("acacia", 1, 1));
		paintingRegistry.register(createPainting("dark_oak", 1, 1));

		Furnish.LOG.info("Registered Furnish Paintings.");
	}
}
