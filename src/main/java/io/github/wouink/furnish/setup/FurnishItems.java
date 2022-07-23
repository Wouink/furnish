package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.util.INoBlockItem;
import io.github.wouink.furnish.block.util.ISpecialItemProperties;
import io.github.wouink.furnish.item.Letter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class FurnishItems {

	public static final CreativeModeTab Furnish_ItemGroup = new CreativeModeTab(Furnish.MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(FurnishBlocks.Furniture_Workbench.get());
		}
	};

	@SubscribeEvent
	public static void onItemRegistry(final RegisterEvent event) {
		event.register(ForgeRegistries.Keys.ITEMS, helper -> {
			for(RegistryObject<Block> b : FurnishBlocks.Registry.getEntries()) {
				if(!(b.get() instanceof INoBlockItem)) {
					if(b.get() instanceof ISpecialItemProperties) {
						helper.register(b.getId(), new BlockItem (b.get(), ((ISpecialItemProperties) b.get()).getProperties()));
					} else helper.register(b.getId(), new BlockItem(b.get(), new Item.Properties().tab(Furnish_ItemGroup)));
				}
			}
			helper.register(new ResourceLocation(Furnish.MODID, "letter"), new Letter(new Item.Properties().tab(Furnish_ItemGroup).stacksTo(1)));
		});

		Furnish.LOG.info("Registered Furnish Items.");
	}
}
