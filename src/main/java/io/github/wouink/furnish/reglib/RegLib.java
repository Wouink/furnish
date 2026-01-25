package io.github.wouink.furnish.reglib;

import com.mojang.serialization.Codec;
import io.github.wouink.furnish.Furnish;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Minecraft's content registration system changes basically every ten minutes.
 * This is an attempt to simplify all the work required for every update.
 */
public class RegLib {
    // holds the items that need to be registered in the creative tab later
    private static List<Item> itemsInCreativeTab = new ArrayList<>();

    /**
     * Registers an item in the game
     * Example use:
     * public static final Item TEST_ITEM = RegLib.registerItem("test_item", Item::new, new Item.Properties());
     * @param id the identifier, without the mod id, example: red_curtain
     * @param itemFactory a reference to the constructor, example: RedCurtainItem::new
     * @param properties item properties
     * @return the item
     * @param <GenericItem> the item's class
     */
    public static <GenericItem extends Item> GenericItem registerItem(
            String id,
            Function<Item.Properties, GenericItem> itemFactory,
            Item.Properties properties
    ) {
        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, id)
        );
        GenericItem item = itemFactory.apply(properties);
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        itemsInCreativeTab.add(item);
        return item;
    }

    /**
     * Registers a block (and optionally an item) in the game
     * @param id the identifier, example: red_curtain
     * @param blockFactory a reference to the block constructor, example RedCurtainBlock::new
     * @param properties the block properties
     * @param registerItem should we automatically create an item for this block?
     * @return the block
     */
    public static Block registerBlock(
            String id,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties properties,
            boolean registerItem
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, id)
        );
        Block block = blockFactory.apply(properties);
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);

        if (registerItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, id)
            );
            BlockItem blockItem = new BlockItem(
                    block,
                    new Item.Properties()
            );
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
            itemsInCreativeTab.add(blockItem);
        }

        return block;
    }

    /**
     * Registers a block entity type in the game
     * @param id the block entity's id
     * @param entityFactory a reference to it's constructor
     * @param blocks an array of the blocks related to this block entity
     * @return the block entity type
     * @param <T> the block entity's class
     */
    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(
            String id,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        return Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, id),
                FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build()
        );
    }

    /**
     * Registers a menu (Screen <-> BlockEntity mapping = slots logic) in the game
     * Example use: MenuType<WorkbenchMenu> WORKBENCH_MENU = RegLib.registerMenuType("workbench", WorkbenchMenu::new);
     * @param id the name of the menu (without namespace)
     * @param menuSupplier the
     * @return
     * @param <T>
     */
    public static <T extends AbstractContainerMenu> MenuType<T> registerMenuType(
            String id,
            MenuType.MenuSupplier<T> menuSupplier
    ) {
        return Registry.register(
                BuiltInRegistries.MENU,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, id),
                new MenuType<>(menuSupplier, FeatureFlagSet.of())
        );
    }

    /**
     * Registers a sound in the game
     * @param soundKey the name of the sound (without namespace) - the sound needs to be defined in sounds.json
     * @return a SoundEvent object
     */
    public static SoundEvent registerSound(String soundKey) {
        ResourceLocation soundIdentifier = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, soundKey);
        return Registry.register(
                BuiltInRegistries.SOUND_EVENT,
                soundIdentifier,
                SoundEvent.createVariableRangeEvent(soundIdentifier)
        );
    }

    /**
     * Registers a new EntityType in the game
     * Example use:
     *     public static EntityType<SeatEntity> SEAT_ENTITY = RegLib.registerEntityType(
     *             "seat",
     *             EntityType.Builder.of((entityType, level)
     *                     -> new SeatEntity(level), MobCategory.MISC).sized(0f, 0f)
     *     );
     * @param name the name of the entity (without namespace)
     * @param builder the builder
     * @return the EntityType
     * @param <T> the EntityType
     */
    public static <T extends Entity> EntityType<T> registerEntityType(String name, EntityType.Builder builder) {
        return Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, name),
                builder.build(name)
        );
    }

    /**
     * Registers a new tag in the game
     * @param tagType what kind of tag? block tag? item tag?
     * @param name the name of the tag (without namespace)
     * @return the tag
     */
    public static TagKey registerTag(ResourceKey tagType, String name) {
        return TagKey.create(tagType, ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, name));
    }

    /**
     * Registers a creative tab in the game
     * @param name the name of the creative tag
     * @param icon the item used as an icon for the creative tag
     * @return a creative tab with the translation key of `itemGroup.MOD_ID.name`
     */
    public static CreativeModeTab registerCreativeTab(String name, Item icon) {
        ResourceKey<CreativeModeTab> key = ResourceKey.create(
                BuiltInRegistries.CREATIVE_MODE_TAB.key(),
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, name)
        );
        CreativeModeTab tab = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                key,
                FabricItemGroup.builder()
                        .icon(() -> new ItemStack(icon))
                        .title(Component.translatable("itemGroup." + Furnish.MOD_ID + "." + name))
                        .build()
        );
        ItemGroupEvents.modifyEntriesEvent(key).register(RegLib::addItemsToTab);
        return tab;
    }

    private static void addItemsToTab(FabricItemGroupEntries itemGroup) {
        for(Item i : itemsInCreativeTab) itemGroup.accept(i);
        // free the list, we don't need it anymore
        itemsInCreativeTab = null;
    }

    /**
     * Registers a new recipe in the game
     * Example: RecipeType<FurnitureRecipe> FURNITURE_RECIPE = RegLib.registerRecipeType("furniture_making");
     * @param name the name of the recipe type (without namespace)
     * @return the recipe type
     * @param <T> the class for the recipe type
     */
    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(String name) {
        RecipeType<T> recipeType = new RecipeType<T>() {
            @Override
            public String toString() {
                return Furnish.MOD_ID + ":" + name;
            }
        };
        return Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, name),
                recipeType
        );
    }

    /**
     * Registers a new recipe serializer
     * @param name the name of the recipe serializer (without namespace)
     * @param serializer the serializer object
     * @return the serializer
     * @param <T> the class for the serializer
     */
    public static <T extends Recipe<?>> RecipeSerializer<T> registerRecipeSerializer(
            String name,
            RecipeSerializer<T> serializer
    ) {
        return Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "furniture_making"),
                serializer
        );
    }

    /**
     * Registers a data component type in the game
     * @param name the name of the data component type (without namespace)
     * @return the data component type
     * TODO add support for other types - as of now, only supports String as Furnish doesn't use any other
     */
    public static DataComponentType<String> registerDataComponentType(String name) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, name),
                DataComponentType.<String>builder()
                        .persistent(Codec.STRING)
                        .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                        .build()
        );
    }

    /**
     * Registers a network message
     * @param dir the direction (either S2C or C2S)
     * @param type the type of the message
     * @param codec the codec for the message
     */
    public static void registerNetworkMessage(MessageDirection dir, CustomPacketPayload.Type type, StreamCodec codec) {
        if(dir == MessageDirection.S2C)
            PayloadTypeRegistry.configurationS2C().register(type, codec);
        else
            PayloadTypeRegistry.configurationC2S().register(type, codec);
    }

    public enum MessageDirection {
        S2C, C2S
    }
}
