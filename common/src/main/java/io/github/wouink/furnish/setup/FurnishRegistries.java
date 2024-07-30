package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.Crate;
import io.github.wouink.furnish.block.Mailbox;
import io.github.wouink.furnish.block.container.*;
import io.github.wouink.furnish.block.blockentity.*;
import io.github.wouink.furnish.entity.SeatEntity;
import io.github.wouink.furnish.recipe.FurnitureRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FurnishRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Furnish.MODID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Furnish.MODID, Registries.ITEM);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Furnish.MODID, Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Furnish.MODID, Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Furnish.MODID, Registries.SOUND_EVENT);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Furnish.MODID, Registries.MENU);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Furnish.MODID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Furnish.MODID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Furnish.MODID, Registries.PAINTING_VARIANT);

    // Tags
    public static final TagKey CRATE_BLACKLIST_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "crate_blacklist"));
    public static final TagKey MUSIC_DISCS_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "music_discs"));
    public static final TagKey FOOD_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "food"));
    public static final TagKey PLANTS_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "plants"));
    public static final TagKey MAIL_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "mail"));
    public static final TagKey BOOKS_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "books"));

    // Mailbox configuration is done with tags
    public static final TagKey BYPASSES_MAIL_TAG_TAG = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "bypasses_mail_tag"));
    public static final TagKey NON_OP_CREATIVE_CAN_DESTROY_TAG = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "non_op_creative_can_destroy"));

    // Knock on door configuration also with tag
    public static final TagKey CAN_KNOCK_ON = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "can_knock_on"));

    // Same thing for popping lectern book
    public static final TagKey CAN_POP_BOOK = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "can_pop_book"));

    // And for painting cycling
    public static final TagKey CAN_CYCLE = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "can_cycle"));

    // And for placing carpets/snow on stairs/trappdoors/fences
    public static final TagKey PLACE_ON_STAIRS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "place_on_stairs"));
    public static final TagKey PLACE_ON_TRAPDOOR = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "place_on_trapdoor"));
    public static final TagKey PLACE_ON_FENCE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, "place_on_fence"));

    // Recipe related registry objects
    public static final RegistrySupplier<RecipeType<FurnitureRecipe>> Furniture_Recipe = FurnishRegistries.RECIPE_TYPES.register(
            "furniture_making",
            () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "furniture_making";
                }
            }
    );

    // SingleItemRecipe.Serializer is made public with access wideners
    public static final RecipeSerializer<SingleItemRecipe> Furniture_Recipe_Serializer = RecipeSerializer.register("furniture_making", new SingleItemRecipe.Serializer<FurnitureRecipe>(FurnitureRecipe::new));

    // Containers

    public static final RegistrySupplier<MenuType<FurnitureWorkbenchContainer>> Furniture_Workbench_Container = FurnishRegistries.CONTAINERS.register(
            "furniture_workbench",
            () -> new MenuType<>(FurnitureWorkbenchContainer::new, FeatureFlagSet.of())
            // todo FeatureFlagSet.of what?
    );
    public static final RegistrySupplier<MenuType<CrateContainer>> Crate_Container = FurnishRegistries.CONTAINERS.register(
            "crate",
            () -> new MenuType<>(CrateContainer::new, FeatureFlagSet.of())
    );
    public static final RegistrySupplier<MenuType<MailboxContainer>> Mailbox_Container = FurnishRegistries.CONTAINERS.register(
            "mailbox",
            () -> new MenuType<>(MailboxContainer::new, FeatureFlagSet.of())
    );
    public static final RegistrySupplier<MenuType<DiskRackContainer>> Disk_Rack_Container = FurnishRegistries.CONTAINERS.register(
            "disk_rack",
            () -> new MenuType<>(DiskRackContainer::new, FeatureFlagSet.of())
    );
    public static final RegistrySupplier<MenuType<BookshelfChestContainer>> Bookshelf_Chest_Container = FurnishRegistries.CONTAINERS.register(
            "bookshelf_chest",
            () -> new MenuType<>(BookshelfChestContainer::new, FeatureFlagSet.of())
    );

    // Sounds

    public static RegistrySupplier<SoundEvent> registerSoundEvent(String soundKey) {
        // todo variable range event? fixed range event?
        return SOUND_EVENTS.register(soundKey, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Furnish.MODID, soundKey)));
    }

    public static final RegistrySupplier<SoundEvent> Cabinet_Open_Sound = FurnishRegistries.registerSoundEvent("block.furniture.open");
    public static final RegistrySupplier<SoundEvent> Cabinet_Close_Sound = FurnishRegistries.registerSoundEvent("block.furniture.close");
    public static final RegistrySupplier<SoundEvent> Spruce_Cabinet_Open_Sound = FurnishRegistries.registerSoundEvent("block.furniture_spruce.open");
    public static final RegistrySupplier<SoundEvent> Spruce_Cabinet_Close_Sound = FurnishRegistries.registerSoundEvent("block.furniture_spruce.close");
    public static final RegistrySupplier<SoundEvent> Drawers_Open_Sound = FurnishRegistries.registerSoundEvent("block.furniture_drawers.open");
    public static final RegistrySupplier<SoundEvent> Drawers_Close_Sound = FurnishRegistries.registerSoundEvent("block.furniture_drawers.close");
    public static final RegistrySupplier<SoundEvent> Locker_Open_Sound = FurnishRegistries.registerSoundEvent("block.furniture_locker.open");
    public static final RegistrySupplier<SoundEvent> Locker_Close_Sound = FurnishRegistries.registerSoundEvent("block.furniture_locker.close");
    public static final RegistrySupplier<SoundEvent> Amphora_Open_Sound = FurnishRegistries.registerSoundEvent("block.amphora.open");
    public static final RegistrySupplier<SoundEvent> Amphora_Close_Sound = FurnishRegistries.registerSoundEvent("block.amphora.close");
    public static final RegistrySupplier<SoundEvent> Wooden_Door_Knock_Sound = FurnishRegistries.registerSoundEvent("event.knock_on_door.wood");
    public static final RegistrySupplier<SoundEvent> Iron_Door_Knock_Sound = FurnishRegistries.registerSoundEvent("event.knock_on_door.iron");
    public static final RegistrySupplier<SoundEvent> Mailbox_Update_Sound = FurnishRegistries.registerSoundEvent("block.mailbox.update");
    public static final RegistrySupplier<SoundEvent> Attach_To_Letter_Sound = FurnishRegistries.registerSoundEvent("item.letter.add_attachment");
    public static final RegistrySupplier<SoundEvent> Detach_From_Letter_Sound = FurnishRegistries.registerSoundEvent("item.letter.remove_attachment");
    public static final RegistrySupplier<SoundEvent> Curtain_Sound = FurnishRegistries.registerSoundEvent("block.curtain.interact");
    public static final RegistrySupplier<SoundEvent> Recycle_Bin_Empty_Sound = FurnishRegistries.registerSoundEvent("block.recycle_bin.empty");
    public static final RegistrySupplier<SoundEvent> Trash_Can_Empty_Sound = FurnishRegistries.registerSoundEvent("block.trash_can.empty");
    public static final RegistrySupplier<SoundEvent> Iron_Gate_Open_Sound = FurnishRegistries.registerSoundEvent("block.iron_gate.open");
    public static final RegistrySupplier<SoundEvent> Iron_Gate_Close_Sound = FurnishRegistries.registerSoundEvent("block.iron_gate.close");
    public static final RegistrySupplier<SoundEvent> Mail_Received_Sound = FurnishRegistries.registerSoundEvent("event.mail_received");

    public static SoundType Paper_Sound_Type = new SoundType(1.0f, 1.0f, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN);

    // Entities

    public static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }

    public static final RegistrySupplier<EntityType<SeatEntity>> Seat_Entity = FurnishRegistries.registerEntityType("seat", EntityType.Builder.<SeatEntity>of((type, world) -> new SeatEntity(world), MobCategory.MISC).sized(0.0f, 0.0f));

    // Block entities

    public static final RegistrySupplier<BlockEntityType<FurnitureBlockEntity>> Furniture_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("furniture", () -> BlockEntityType.Builder.of(FurnitureBlockEntity::new, FurnishBlocks.Furniture_3x9.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<LargeFurnitureBlockEntity>> Large_Furniture_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("large_furniture", () -> BlockEntityType.Builder.of(LargeFurnitureBlockEntity::new, FurnishBlocks.Furniture_6x9.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<AmphoraBlockEntity>> Amphora_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("amphora", () -> BlockEntityType.Builder.of(AmphoraBlockEntity::new, FurnishBlocks.Amphorae.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<MailboxBlockEntity>> Mailbox_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("mailbox", () -> BlockEntityType.Builder.of(MailboxBlockEntity::new, Mailbox.All_Mailboxes.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<CrateBlockEntity>> Crate_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("crate", () -> BlockEntityType.Builder.of(CrateBlockEntity::new, Crate.All_Crates.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<PlateBlockEntity>> Plate_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("plate", () -> BlockEntityType.Builder.of(PlateBlockEntity::new, FurnishBlocks.Plates.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<ShelfBlockEntity>> Shelf_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("shelf", () -> BlockEntityType.Builder.of(ShelfBlockEntity::new, FurnishBlocks.Shelves.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<ShowcaseBlockEntity>> Showcase_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("showcase", () -> BlockEntityType.Builder.of(ShowcaseBlockEntity::new, FurnishBlocks.Showcases.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<DiskRackBlockEntity>> Disk_Rack_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("disk_rack", () -> BlockEntityType.Builder.of(DiskRackBlockEntity::new, new Block[]{FurnishBlocks.Disk_Rack.get()}).build(null));
    public static final RegistrySupplier<BlockEntityType<RecycleBinBlockEntity>> Recycle_Bin_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("recycle_bin", () -> BlockEntityType.Builder.of(RecycleBinBlockEntity::new, FurnishBlocks.Recycle_Bins.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<FlowerPotBlockEntity>> Flower_Pot_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("flower_pot", () -> BlockEntityType.Builder.of(FlowerPotBlockEntity::new, FurnishBlocks.Flower_Pots.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<BookshelfChestBlockEntity>> BookshelfChest_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("bookshelf_chest", () -> BlockEntityType.Builder.of(BookshelfChestBlockEntity::new, FurnishBlocks.Bookshelf_Chests.toArray(Block[]::new)).build(null));

    // Paintings

    private static RegistrySupplier<PaintingVariant> newPainting(int width, int height, String art) {
        return FurnishRegistries.PAINTING_VARIANTS.register(art, () -> new PaintingVariant(width, height, ResourceLocation.fromNamespaceAndPath(Furnish.MODID, art)));
    }

    public static final RegistrySupplier<PaintingVariant> Steve_Painting = newPainting(16, 16, "steve");
    public static final RegistrySupplier<PaintingVariant> Alex_Painting = newPainting(16, 16, "alex");
    public static final RegistrySupplier<PaintingVariant> Oak_Painting = newPainting(16, 16, "oak");
    public static final RegistrySupplier<PaintingVariant> Birch_Painting = newPainting(16, 16, "birch");
    public static final RegistrySupplier<PaintingVariant> Spruce_Painting = newPainting(16, 32, "spruce");
    public static final RegistrySupplier<PaintingVariant> Jungle_Painting = newPainting(16, 32, "jungle");
    public static final RegistrySupplier<PaintingVariant> Acacia_Painting = newPainting(16, 16, "acacia");
    public static final RegistrySupplier<PaintingVariant> Dark_Oak_Painting = newPainting(16, 16, "dark_oak");
    public static final RegistrySupplier<PaintingVariant> Trader_Llama_Painting = newPainting(16, 16, "trader_llama");
    public static final RegistrySupplier<PaintingVariant> Owl_Painting = newPainting(32, 16, "owl");
    public static final RegistrySupplier<PaintingVariant> Fox_Painting = newPainting(32, 32, "fox");
    public static final RegistrySupplier<PaintingVariant> Cork_Board = newPainting(32, 16, "cork_board");
    public static final RegistrySupplier<PaintingVariant> Cork_Board_1 = newPainting(32, 16, "cork_board_1");
    public static final RegistrySupplier<PaintingVariant> Large_Cork_Board = newPainting(48, 32, "large_cork_board");
    public static final RegistrySupplier<PaintingVariant> Large_Cork_Board_1 = newPainting(48, 32, "large_cork_board_1");
    public static final RegistrySupplier<PaintingVariant> Investigation_Cork_Board = newPainting(32, 16, "investigation_board");
    public static final RegistrySupplier<PaintingVariant> Large_Investigation_Cork_Board = newPainting(48, 32, "large_investigation_board");
}
