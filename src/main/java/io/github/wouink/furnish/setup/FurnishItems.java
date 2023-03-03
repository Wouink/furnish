package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishItems {

	public static final DeferredRegister<Item> Registry = DeferredRegister.create(Registries.ITEM, Furnish.MODID);
	public static CreativeModeTab Furnish_ItemGroup;
	public static boolean creativeTabRegistered = false;

	@SubscribeEvent
	public static void onCreativeTagRegistry(CreativeModeTabEvent.Register event) {
		if(!creativeTabRegistered) Furnish_ItemGroup = event.registerCreativeModeTab(new ResourceLocation(Furnish.MODID, "furnish"),
				builder -> builder.icon(() -> new ItemStack(FurnishBlocks.Furniture_Workbench.get()))
						.displayItems((featureFlags, output, hasOpPermissions) -> Registry.getEntries().forEach(item -> output.accept(item.get())))
						.title(Component.translatable("itemGroup.furnish"))
		);
		creativeTabRegistered = true;
		Furnish.LOG.info("Registered Furnish Creative Tab.");
	}

	public static final RegistryObject<Item> Letter = Registry.register("letter", () -> new Letter(new Item.Properties().stacksTo(1)));
}
