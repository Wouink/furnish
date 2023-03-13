package io.github.wouink.furnish.setup;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FurnishRegistries {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Furnish.MODID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Furnish.MODID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Furnish.MODID, Registry.RECIPE_TYPE_REGISTRY);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Furnish.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Furnish.MODID, Registry.SOUND_EVENT_REGISTRY);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Furnish.MODID, Registry.MENU_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Furnish.MODID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Furnish.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Furnish.MODID, Registry.PAINTING_VARIANT_REGISTRY);

    public static RegistrySupplier<Block> registerBlock(String name, Supplier<? extends Block> sup) {
        return BLOCKS.register(name, sup);
    }

    public static RegistrySupplier<Block> registerBlockWithItem(String name, Supplier<? extends Block> sup, Item.Properties itemProps) {
        RegistrySupplier<Block> registeredBlock = BLOCKS.register(name, sup);
        ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), itemProps));
        return registeredBlock;
    }

    public static RegistrySupplier<Block> registerBlockWithItem(String name, Supplier<? extends Block> sup) {
        return registerBlockWithItem(name, sup, new Item.Properties());
    }

    public static RegistrySupplier<SoundEvent> registerSoundEvent(String soundKey) {
        return SOUND_EVENTS.register(soundKey, () -> new SoundEvent(new ResourceLocation(Furnish.MODID, soundKey)));
    }

    public static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(name));
    }
}
