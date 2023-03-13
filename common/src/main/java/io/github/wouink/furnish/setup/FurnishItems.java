package io.github.wouink.furnish.setup;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FurnishItems {

	public static final CreativeModeTab Furnish_ItemGroup = CreativeTabRegistry.create(new ResourceLocation(Furnish.MODID, "furnish"), () -> new ItemStack(FurnishBlocks.Furniture_Workbench.get()));

	public static final RegistrySupplier<Item> Letter = FurnishRegistries.ITEMS.register("letter", () -> new Letter(new Item.Properties().tab(Furnish_ItemGroup).stacksTo(1)));
}
