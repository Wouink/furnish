package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.util.INoBlockItem;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishItems {

	public static final CreativeModeTab Furnish_ItemGroup = new CreativeModeTab(Furnish.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(FurnishBlocks.Furniture_Workbench.get());
		}
	};

	public static BlockItem getBlockItem(Block block) {
		return (BlockItem) new BlockItem(block, new Item.Properties().tab(Furnish_ItemGroup)).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	public static BlockItem getBlockItemWithProperties(Block block, Item.Properties properties) {
		return (BlockItem) new BlockItem(block, properties).setRegistryName(Objects.requireNonNull(block.getRegistryName()));
	}

	@SubscribeEvent
	public static void onItemRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
		IForgeRegistry<Item> itemRegistry = itemRegistryEvent.getRegistry();
		for(RegistryObject<Block> b : FurnishBlocks.Registry.getEntries()) {
			if(!(b.get() instanceof INoBlockItem)) {
				if(b.get() instanceof ISpecialItemProperties) {
					itemRegistry.register(getBlockItemWithProperties(b.get(), ((ISpecialItemProperties) b.get()).getProperties()));
				} else itemRegistry.register(getBlockItem(b.get()));
			}
		}
		itemRegistry.register(new Letter(new Item.Properties().tab(Furnish_ItemGroup).stacksTo(1), "letter"));
		Furnish.LOG.info("Registered Furnish Items.");
	}
}
