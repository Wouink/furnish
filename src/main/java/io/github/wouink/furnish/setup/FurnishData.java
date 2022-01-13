package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.Crate;
import io.github.wouink.furnish.block.Mailbox;
import io.github.wouink.furnish.block.container.*;
import io.github.wouink.furnish.block.tileentity.*;
import io.github.wouink.furnish.client.gui.ConditionalSlotContainerScreen;
import io.github.wouink.furnish.client.gui.DiskRackScreen;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.gui.PotionShelfScreen;
import io.github.wouink.furnish.client.renderer.*;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.recipe.FSingleItemRecipe;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishData {



	public static final IRecipeType<FurnitureRecipe> Furniture_Recipe = IRecipeType.register(Furnish.MODID + ":furniture_making");

	public static class RecipeSerializers {
		public static final DeferredRegister<IRecipeSerializer<?>> Registry = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Furnish.MODID);
		public static final RegistryObject<IRecipeSerializer<FurnitureRecipe>> Furniture_Recipe_Serializer = Registry.register(
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
		public static final RegistryObject<SoundEvent> Amphora_Open = registerSound("block.amphora.open");
		public static final RegistryObject<SoundEvent> Amphora_Close = registerSound("block.amphora.close");
		public static final RegistryObject<SoundEvent> Wooden_Door_Knock = registerSound("event.knock_on_door.wood");
		public static final RegistryObject<SoundEvent> Iron_Door_Knock = registerSound("event.knock_on_door.iron");
		public static final RegistryObject<SoundEvent> Mailbox_Update = registerSound("block.mailbox.update");
		public static final RegistryObject<SoundEvent> Attach_To_Letter = registerSound("item.letter.add_attachment");
		public static final RegistryObject<SoundEvent> Detach_From_Letter = registerSound("item.letter.remove_attachment");
		public static final RegistryObject<SoundEvent> Drum_Tom = registerSound("instrument.drum.tom");
		public static final RegistryObject<SoundEvent> Drum_Snare = registerSound("instrument.drum.snare");
		public static final RegistryObject<SoundEvent> Cymbal = registerSound("instrument.cymbal");
		public static final RegistryObject<SoundEvent> Cymbal_Hihat = registerSound("instrument.cymbal.hihat");
		public static final RegistryObject<SoundEvent> Curtain = registerSound("block.curtain.interact");

		private static RegistryObject<SoundEvent> registerSound(String key) {
			return Registry.register(key, () -> new SoundEvent(new ResourceLocation(Furnish.MODID, key)));
		}
	}

	public static class Containers {
		public static final DeferredRegister<ContainerType<?>> Registry = DeferredRegister.create(ForgeRegistries.CONTAINERS, Furnish.MODID);
		public static final RegistryObject<ContainerType<FurnitureWorkbenchContainer>> Furniture_Workbench = Registry.register("furniture_workbench",
				() -> new ContainerType<>(FurnitureWorkbenchContainer::new)
		);
		public static final RegistryObject<ContainerType<CrateContainer>> Crate = Registry.register("crate",
				() -> new ContainerType<>(CrateContainer::new)
		);
		public static final RegistryObject<ContainerType<MailboxContainer>> Mailbox = Registry.register("mailbox",
				() -> new ContainerType<>(MailboxContainer::new)
		);
		public static final RegistryObject<ContainerType<CookingPotContainer>> Cooking_Pot = Registry.register("cooking_pot",
				() -> new ContainerType<>(CookingPotContainer::new)
		);
		public static final RegistryObject<ContainerType<DiskRackContainer>> Disk_Rack = Registry.register("disk_rack",
				() -> new ContainerType<>(DiskRackContainer::new)
		);
		public static final RegistryObject<ContainerType<PotionShelfContainer>> Potion_Shelf = Registry.register("potion_shelf",
				() -> new ContainerType<>(PotionShelfContainer::new)
		);
	}

	public static class Entities {
		public static final DeferredRegister<EntityType<?>> Registry = DeferredRegister.create(ForgeRegistries.ENTITIES, Furnish.MODID);
		public static final RegistryObject<EntityType<SeatEntity>> Seat_Entity = register("seat",
				EntityType.Builder.<SeatEntity>of((type, world) -> new SeatEntity(world), EntityClassification.MISC)
						.sized(0.0f, 0.0f).setCustomClientFactory(((spawnEntity, world) -> new SeatEntity(world)))
		);

		private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
			return Registry.register(name, () -> builder.build(name));
		}
	}

	public static class TileEntities {
		public static final DeferredRegister<TileEntityType<?>> Registry = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Furnish.MODID);
		public static final RegistryObject<TileEntityType<FurnitureTileEntity>> TE_Furniture = register("furniture", FurnitureTileEntity::new, () -> FurnishBlocks.FurnitureInvProvider);
		public static final RegistryObject<TileEntityType<LargeFurnitureTileEntity>> TE_Large_Furniture = register("large_furniture", LargeFurnitureTileEntity::new, () -> FurnishBlocks.FurnitureLargeInvProvider);
		public static final RegistryObject<TileEntityType<AmphoraTileEntity>> TE_Amphora = register("amphora", AmphoraTileEntity::new, () -> FurnishBlocks.Amphorae);
		public static final RegistryObject<TileEntityType<MailboxTileEntity>> TE_Mailbox = register("mailbox", MailboxTileEntity::new, () -> Mailbox.All_Mailboxes.toArray(new Mailbox[0]));
		public static final RegistryObject<TileEntityType<CrateTileEntity>> TE_Crate = register("crate", CrateTileEntity::new, () -> Crate.All_Crates.toArray(new Crate[0]));
		public static final RegistryObject<TileEntityType<CookingPotTileEntity>> TE_Cooking_Pot = register("cooking_pot", CookingPotTileEntity::new, () -> FurnishBlocks.Cooking_Pots);
		public static final RegistryObject<TileEntityType<PlateTileEntity>> TE_Plate = register("plate", PlateTileEntity::new, () -> FurnishBlocks.Plates);
		public static final RegistryObject<TileEntityType<ShelfTileEntity>> TE_Shelf = register("shelf", ShelfTileEntity::new, () -> FurnishBlocks.Shelves);
		public static final RegistryObject<TileEntityType<ShowcaseTileEntity>> TE_Showcase = register("showcase", ShowcaseTileEntity::new, () -> FurnishBlocks.Showcases);
		public static final RegistryObject<TileEntityType<DiskRackTileEntity>> TE_Disk_Rack = register("disk_rack", DiskRackTileEntity::new, () -> FurnishBlocks.Disk_Racks);
		public static final RegistryObject<TileEntityType<PotionShelfTileEntity>> TE_Potion_Shelf = register("potion_shelf", PotionShelfTileEntity::new, () -> FurnishBlocks.Potion_Shelves);

		private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, Supplier<Block[]> validBlockSupplier) {
			return Registry.register(name, () -> TileEntityType.Builder.of(factory, validBlockSupplier.get()).build(null));
		}
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
		ScreenManager.register(Containers.Furniture_Workbench.get(), FurnitureWorkbenchScreen::new);
		ScreenManager.register(Containers.Crate.get(), ConditionalSlotContainerScreen::new);
		ScreenManager.register(Containers.Mailbox.get(), ConditionalSlotContainerScreen::new);
		ScreenManager.register(Containers.Cooking_Pot.get(), ConditionalSlotContainerScreen::new);
		ScreenManager.register(Containers.Disk_Rack.get(), DiskRackScreen::new);
		ScreenManager.register(Containers.Potion_Shelf.get(), PotionShelfScreen::new);
		Furnish.LOG.info("Registered Furnish Screens.");

		RenderingRegistry.registerEntityRenderingHandler(Entities.Seat_Entity.get(), SeatRenderer::new);
		Furnish.LOG.info("Registered Furnish Entity Renderers.");

		ClientRegistry.bindTileEntityRenderer(TileEntities.TE_Mailbox.get(), MailboxRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntities.TE_Plate.get(), PlateRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntities.TE_Shelf.get(), ShelfRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntities.TE_Showcase.get(), ShowcaseRenderer::new);
		ClientRegistry.bindTileEntityRenderer(TileEntities.TE_Disk_Rack.get(), DiskRackRenderer::new);
		Furnish.LOG.info("Registered Furnish TileEntity Renderers.");
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

		// Furnish.LOG.info("Registering Furnish custom paintings...");
		// CustomPaintings.registerCustomPaintings(paintingRegistry, "config/furnish_custom_paintings.json");
	}
}
