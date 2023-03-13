package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.block.Crate;
import io.github.wouink.furnish.block.Mailbox;
import io.github.wouink.furnish.block.container.CrateContainer;
import io.github.wouink.furnish.block.container.DiskRackContainer;
import io.github.wouink.furnish.block.container.FurnitureWorkbenchContainer;
import io.github.wouink.furnish.block.container.MailboxContainer;
import io.github.wouink.furnish.block.tileentity.*;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.recipe.FSingleItemRecipe;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FurnishData {
	public static class RecipeTypes {
		public static final RegistrySupplier<RecipeType<FurnitureRecipe>> Furniture_Recipe = FurnishRegistries.RECIPE_TYPES.register(
				"furniture_making",
				() -> new RecipeType<>() {
					@Override
					public String toString() {
						return "furniture_making";
					}
				}
		);
	}

	public static class RecipeSerializers {
		public static final RegistrySupplier<RecipeSerializer<FurnitureRecipe>> Furniture_Recipe_Serializer = FurnishRegistries.RECIPE_SERIALIZERS.register(
				"furniture_making",
				() -> new FSingleItemRecipe.Serializer<>(FurnitureRecipe::new)
		);
	}

	public static class Sounds {
		public static final RegistrySupplier<SoundEvent> Cabinet_Open = FurnishRegistries.registerSoundEvent("block.furniture.open");
		public static final RegistrySupplier<SoundEvent> Cabinet_Close = FurnishRegistries.registerSoundEvent("block.furniture.close");
		public static final RegistrySupplier<SoundEvent> Spruce_Cabinet_Open = FurnishRegistries.registerSoundEvent("block.furniture_spruce.open");
		public static final RegistrySupplier<SoundEvent> Spruce_Cabinet_Close = FurnishRegistries.registerSoundEvent("block.furniture_spruce.close");
		public static final RegistrySupplier<SoundEvent> Drawers_Open = FurnishRegistries.registerSoundEvent("block.furniture_drawers.open");
		public static final RegistrySupplier<SoundEvent> Drawers_Close = FurnishRegistries.registerSoundEvent("block.furniture_drawers.close");
		public static final RegistrySupplier<SoundEvent> Locker_Open = FurnishRegistries.registerSoundEvent("block.furniture_locker.open");
		public static final RegistrySupplier<SoundEvent> Locker_Close = FurnishRegistries.registerSoundEvent("block.furniture_locker.close");
		public static final RegistrySupplier<SoundEvent> Amphora_Open = FurnishRegistries.registerSoundEvent("block.amphora.open");
		public static final RegistrySupplier<SoundEvent> Amphora_Close = FurnishRegistries.registerSoundEvent("block.amphora.close");
		public static final RegistrySupplier<SoundEvent> Wooden_Door_Knock = FurnishRegistries.registerSoundEvent("event.knock_on_door.wood");
		public static final RegistrySupplier<SoundEvent> Iron_Door_Knock = FurnishRegistries.registerSoundEvent("event.knock_on_door.iron");
		public static final RegistrySupplier<SoundEvent> Mailbox_Update = FurnishRegistries.registerSoundEvent("block.mailbox.update");
		public static final RegistrySupplier<SoundEvent> Attach_To_Letter = FurnishRegistries.registerSoundEvent("item.letter.add_attachment");
		public static final RegistrySupplier<SoundEvent> Detach_From_Letter = FurnishRegistries.registerSoundEvent("item.letter.remove_attachment");
		public static final RegistrySupplier<SoundEvent> Curtain = FurnishRegistries.registerSoundEvent("block.curtain.interact");
		public static final RegistrySupplier<SoundEvent> Recycle_Bin_Empty = FurnishRegistries.registerSoundEvent("block.recycle_bin.empty");
		public static final RegistrySupplier<SoundEvent> Trash_Can_Empty = FurnishRegistries.registerSoundEvent("block.trash_can.empty");
		public static final RegistrySupplier<SoundEvent> Iron_Gate_Open = FurnishRegistries.registerSoundEvent("block.iron_gate.open");
		public static final RegistrySupplier<SoundEvent> Iron_Gate_Close = FurnishRegistries.registerSoundEvent("block.iron_gate.close");
		public static final RegistrySupplier<SoundEvent> Mail_Received = FurnishRegistries.registerSoundEvent("event.mail_received");
	}

	public static class SoundTypes {
		// public static final SoundType Paper = new ForgeSoundType(1.0f, 1.0f, break, step, place, hit, fall);
	}

	public static class Containers {
		public static final RegistrySupplier<MenuType<FurnitureWorkbenchContainer>> Furniture_Workbench = FurnishRegistries.CONTAINERS.register(
				"furniture_workbench",
				() -> new MenuType<>(FurnitureWorkbenchContainer::new)
		);
		public static final RegistrySupplier<MenuType<CrateContainer>> Crate = FurnishRegistries.CONTAINERS.register(
				"crate",
				() -> new MenuType<>(CrateContainer::new)
		);
		public static final RegistrySupplier<MenuType<MailboxContainer>> Mailbox = FurnishRegistries.CONTAINERS.register(
				"mailbox",
				() -> new MenuType<>(MailboxContainer::new)
		);
		public static final RegistrySupplier<MenuType<DiskRackContainer>> Disk_Rack = FurnishRegistries.CONTAINERS.register(
				"disk_rack",
				() -> new MenuType<>(DiskRackContainer::new)
		);
	}

	public static class Entities {
		public static final RegistrySupplier<EntityType<SeatEntity>> Seat_Entity = FurnishRegistries.registerEntityType("seat", EntityType.Builder.<SeatEntity>of((type, world) -> new SeatEntity(world), MobCategory.MISC).sized(0.0f, 0.0f));
	}

	public static class TileEntities {
		public static final RegistrySupplier<BlockEntityType<FurnitureTileEntity>> TE_Furniture = FurnishRegistries.BLOCK_ENTITIES.register("furniture", () -> BlockEntityType.Builder.of(FurnitureTileEntity::new, FurnishBlocks.Furniture_3x9.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<LargeFurnitureTileEntity>> TE_Large_Furniture = FurnishRegistries.BLOCK_ENTITIES.register("large_furniture", () -> BlockEntityType.Builder.of(LargeFurnitureTileEntity::new, FurnishBlocks.Furniture_6x9.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<AmphoraTileEntity>> TE_Amphora = FurnishRegistries.BLOCK_ENTITIES.register("amphora", () -> BlockEntityType.Builder.of(AmphoraTileEntity::new, FurnishBlocks.Amphorae.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<MailboxTileEntity>> TE_Mailbox = FurnishRegistries.BLOCK_ENTITIES.register("mailbox", () -> BlockEntityType.Builder.of(MailboxTileEntity::new, Mailbox.All_Mailboxes.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<CrateTileEntity>> TE_Crate = FurnishRegistries.BLOCK_ENTITIES.register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, Crate.All_Crates.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<PlateTileEntity>> TE_Plate = FurnishRegistries.BLOCK_ENTITIES.register("plate", () -> BlockEntityType.Builder.of(PlateTileEntity::new, FurnishBlocks.Plates.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<ShelfTileEntity>> TE_Shelf = FurnishRegistries.BLOCK_ENTITIES.register("shelf", () -> BlockEntityType.Builder.of(ShelfTileEntity::new, FurnishBlocks.Shelves.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<ShowcaseTileEntity>> TE_Showcase = FurnishRegistries.BLOCK_ENTITIES.register("showcase", () -> BlockEntityType.Builder.of(ShowcaseTileEntity::new, FurnishBlocks.Showcases.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<DiskRackTileEntity>> TE_Disk_Rack = FurnishRegistries.BLOCK_ENTITIES.register("disk_rack", () -> BlockEntityType.Builder.of(DiskRackTileEntity::new, new Block[]{FurnishBlocks.Disk_Rack.get()}).build(null));
		public static final RegistrySupplier<BlockEntityType<RecycleBinTileEntity>> TE_Recycle_Bin = FurnishRegistries.BLOCK_ENTITIES.register("recycle_bin", () -> BlockEntityType.Builder.of(RecycleBinTileEntity::new, FurnishBlocks.Recycle_Bins.toArray(Block[]::new)).build(null));
		public static final RegistrySupplier<BlockEntityType<FlowerPotTileEntity>> TE_Flower_Pot = FurnishRegistries.BLOCK_ENTITIES.register("flower_pot", () -> BlockEntityType.Builder.of(FlowerPotTileEntity::new, FurnishBlocks.Flower_Pots.toArray(Block[]::new)).build(null));
	}

	public static class Paintings {
		public static final RegistrySupplier<PaintingVariant> Steve = FurnishRegistries.PAINTING_VARIANTS.register("steve", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Alex = FurnishRegistries.PAINTING_VARIANTS.register("alex", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Oak = FurnishRegistries.PAINTING_VARIANTS.register("oak", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Birch = FurnishRegistries.PAINTING_VARIANTS.register("birch", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Spruce = FurnishRegistries.PAINTING_VARIANTS.register("spruce", () -> new PaintingVariant(16, 32));
		public static final RegistrySupplier<PaintingVariant> Jungle = FurnishRegistries.PAINTING_VARIANTS.register("jungle", () -> new PaintingVariant(16, 32));
		public static final RegistrySupplier<PaintingVariant> Acacia = FurnishRegistries.PAINTING_VARIANTS.register("acacia", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Dark_Oak = FurnishRegistries.PAINTING_VARIANTS.register("dark_oak", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Trader_Llama = FurnishRegistries.PAINTING_VARIANTS.register("trader_llama", () -> new PaintingVariant(16, 16));
		public static final RegistrySupplier<PaintingVariant> Owl = FurnishRegistries.PAINTING_VARIANTS.register("owl", () -> new PaintingVariant(32, 16));
		public static final RegistrySupplier<PaintingVariant> Fox = FurnishRegistries.PAINTING_VARIANTS.register("fox", () -> new PaintingVariant(32, 32));
	}
}
