package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
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
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FurnishRegistries {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Furnish.MODID, Registry.RECIPE_TYPE_REGISTRY);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Furnish.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Furnish.MODID, Registry.SOUND_EVENT_REGISTRY);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Furnish.MODID, Registry.MENU_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Furnish.MODID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Furnish.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Furnish.MODID, Registry.PAINTING_VARIANT_REGISTRY);

    // Tags
    public static final TagKey CRATE_BLACKLIST_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "crate_blacklist"));
    public static final TagKey MUSIC_DISCS_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "music_discs"));
    public static final TagKey FOOD_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "food"));
    public static final TagKey PLANTS_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "plants"));
    public static final TagKey MAIL_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Furnish.MODID, "mail"));

    // Mailbox configuration is done with tags
    public static final TagKey BYPASSES_MAIL_TAG_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Furnish.MODID, "bypasses_mail_tag"));
    public static final TagKey NON_OP_CREATIVE_CAN_DESTROY_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Furnish.MODID, "non_op_creative_can_destroy"));

    // Knock on door configuration also with tag
    public static final TagKey CAN_KNOCK_ON = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Furnish.MODID, "can_knock_on"));

    // Same thing for popping lectern book
    public static final TagKey CAN_POP_BOOK = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Furnish.MODID, "can_pop_book"));

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

    public static final RegistrySupplier<RecipeSerializer<FurnitureRecipe>> Furniture_Recipe_Serializer = FurnishRegistries.RECIPE_SERIALIZERS.register(
            "furniture_making",
            () -> new FSingleItemRecipe.Serializer<>(FurnitureRecipe::new)
    );

    // Containers

    public static final RegistrySupplier<MenuType<FurnitureWorkbenchContainer>> Furniture_Workbench_Container = FurnishRegistries.CONTAINERS.register(
            "furniture_workbench",
            () -> new MenuType<>(FurnitureWorkbenchContainer::new)
    );
    public static final RegistrySupplier<MenuType<CrateContainer>> Crate_Container = FurnishRegistries.CONTAINERS.register(
            "crate",
            () -> new MenuType<>(CrateContainer::new)
    );
    public static final RegistrySupplier<MenuType<MailboxContainer>> Mailbox_Container = FurnishRegistries.CONTAINERS.register(
            "mailbox",
            () -> new MenuType<>(MailboxContainer::new)
    );
    public static final RegistrySupplier<MenuType<DiskRackContainer>> Disk_Rack_Container = FurnishRegistries.CONTAINERS.register(
            "disk_rack",
            () -> new MenuType<>(DiskRackContainer::new)
    );

    // Sounds

    public static RegistrySupplier<SoundEvent> registerSoundEvent(String soundKey) {
        return SOUND_EVENTS.register(soundKey, () -> new SoundEvent(new ResourceLocation(Furnish.MODID, soundKey)));
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

    public static final RegistrySupplier<BlockEntityType<FurnitureTileEntity>> Furniture_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("furniture", () -> BlockEntityType.Builder.of(FurnitureTileEntity::new, FurnishBlocks.Furniture_3x9.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<LargeFurnitureTileEntity>> Large_Furniture_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("large_furniture", () -> BlockEntityType.Builder.of(LargeFurnitureTileEntity::new, FurnishBlocks.Furniture_6x9.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<AmphoraTileEntity>> Amphora_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("amphora", () -> BlockEntityType.Builder.of(AmphoraTileEntity::new, FurnishBlocks.Amphorae.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<MailboxTileEntity>> Mailbox_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("mailbox", () -> BlockEntityType.Builder.of(MailboxTileEntity::new, Mailbox.All_Mailboxes.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<CrateTileEntity>> Crate_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, Crate.All_Crates.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<PlateTileEntity>> Plate_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("plate", () -> BlockEntityType.Builder.of(PlateTileEntity::new, FurnishBlocks.Plates.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<ShelfTileEntity>> Shelf_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("shelf", () -> BlockEntityType.Builder.of(ShelfTileEntity::new, FurnishBlocks.Shelves.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<ShowcaseTileEntity>> Showcase_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("showcase", () -> BlockEntityType.Builder.of(ShowcaseTileEntity::new, FurnishBlocks.Showcases.stream().map(RegistrySupplier::get).toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<DiskRackTileEntity>> Disk_Rack_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("disk_rack", () -> BlockEntityType.Builder.of(DiskRackTileEntity::new, new Block[]{FurnishBlocks.Disk_Rack.get()}).build(null));
    public static final RegistrySupplier<BlockEntityType<RecycleBinTileEntity>> Recycle_Bin_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("recycle_bin", () -> BlockEntityType.Builder.of(RecycleBinTileEntity::new, FurnishBlocks.Recycle_Bins.toArray(Block[]::new)).build(null));
    public static final RegistrySupplier<BlockEntityType<FlowerPotTileEntity>> Flower_Pot_BlockEntity = FurnishRegistries.BLOCK_ENTITIES.register("flower_pot", () -> BlockEntityType.Builder.of(FlowerPotTileEntity::new, FurnishBlocks.Flower_Pots.toArray(Block[]::new)).build(null));

    // Paintings

    public static final RegistrySupplier<PaintingVariant> Steve_Painting = FurnishRegistries.PAINTING_VARIANTS.register("steve", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Alex_Painting = FurnishRegistries.PAINTING_VARIANTS.register("alex", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Oak_Painting = FurnishRegistries.PAINTING_VARIANTS.register("oak", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Birch_Painting = FurnishRegistries.PAINTING_VARIANTS.register("birch", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Spruce_Painting = FurnishRegistries.PAINTING_VARIANTS.register("spruce", () -> new PaintingVariant(16, 32));
    public static final RegistrySupplier<PaintingVariant> Jungle_Painting = FurnishRegistries.PAINTING_VARIANTS.register("jungle", () -> new PaintingVariant(16, 32));
    public static final RegistrySupplier<PaintingVariant> Acacia_Painting = FurnishRegistries.PAINTING_VARIANTS.register("acacia", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Dark_Oak_Painting = FurnishRegistries.PAINTING_VARIANTS.register("dark_oak", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Trader_Llama_Painting = FurnishRegistries.PAINTING_VARIANTS.register("trader_llama", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> Owl_Painting = FurnishRegistries.PAINTING_VARIANTS.register("owl", () -> new PaintingVariant(32, 16));
    public static final RegistrySupplier<PaintingVariant> Fox_Painting = FurnishRegistries.PAINTING_VARIANTS.register("fox", () -> new PaintingVariant(32, 32));
    public static final RegistrySupplier<PaintingVariant> Cork_Board = FurnishRegistries.PAINTING_VARIANTS.register("cork_board", () -> new PaintingVariant(32, 16));
    public static final RegistrySupplier<PaintingVariant> Cork_Board_1 = FurnishRegistries.PAINTING_VARIANTS.register("cork_board_1", () -> new PaintingVariant(32, 16));
    public static final RegistrySupplier<PaintingVariant> Large_Cork_Board = FurnishRegistries.PAINTING_VARIANTS.register("large_cork_board", () -> new PaintingVariant(48, 32));
    public static final RegistrySupplier<PaintingVariant> Large_Cork_Board_1 = FurnishRegistries.PAINTING_VARIANTS.register("large_cork_board_1", () -> new PaintingVariant(48, 32));
    public static final RegistrySupplier<PaintingVariant> Investigation_Cork_Board = FurnishRegistries.PAINTING_VARIANTS.register("investigation_board", () -> new PaintingVariant(32, 16));
    public static final RegistrySupplier<PaintingVariant> Large_Investigation_Cork_Board = FurnishRegistries.PAINTING_VARIANTS.register("large_investigation_board", () -> new PaintingVariant(48, 32));
}
