package io.github.wouink.furnish.setup;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.client.gui.ConditionalSlotContainerScreen;
import io.github.wouink.furnish.client.gui.DiskRackScreen;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.gui.LetterScreen;
import io.github.wouink.furnish.client.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FurnishClient {
	public static void registerBlockRenderTypes() {
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Red_Bunting.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Yellow_Bunting.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Green_Bunting.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Oak_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Birch_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Jungle_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Spruce_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Acacia_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Dark_Oak_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Crimson_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Warped_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Cherry_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Mangrove_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Bamboo_Shutter.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Recycle_Bin.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Iron_Bars_Top.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Iron_Gate.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Cobweb.get());
		RenderTypeRegistry.register(RenderType.translucent(), FurnishBlocks.Snow_On_Stairs.get());
		for(RegistrySupplier<Block> b : FurnishBlocks.Showcases) RenderTypeRegistry.register(RenderType.translucent(), b.get());
		for(RegistrySupplier<Block> b : FurnishBlocks.Curtains) RenderTypeRegistry.register(RenderType.translucent(), b.get());
		Furnish.LOG.info("Registered Furnish Block Render Types.");
	}

	public static void bindScreensToContainers() {
		// not working on forge since 1.21
		MenuRegistry.registerScreenFactory(FurnishRegistries.Furniture_Workbench_Container.get(), FurnitureWorkbenchScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Crate_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Mailbox_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Bookshelf_Chest_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Disk_Rack_Container.get(), DiskRackScreen::new);
		Furnish.LOG.info("Bound Furnish Containers to their Screens.");
	}

	public static void registerEntityRenderers() {
		// not working on forge since 1.21
		EntityRendererRegistry.register(FurnishRegistries.Seat_Entity::get, SeatRenderer::new);
		Furnish.LOG.info("Registered Furnish Entities Renderers.");

		BlockEntityRendererRegistry.register(FurnishRegistries.Mailbox_BlockEntity.get(), MailboxRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Plate_BlockEntity.get(), PlateRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Shelf_BlockEntity.get(), ShelfRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Showcase_BlockEntity.get(), ShowcaseRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Disk_Rack_BlockEntity.get(), DiskRackRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Recycle_Bin_BlockEntity.get(), RecycleBinRenderer::new);
		Furnish.LOG.info("Registered Furnish Blocks Entities Renderers.");
	}

	public static void openLetterGui(ItemStack stack, Player player, InteractionHand hand) {
		Minecraft.getInstance().setScreen(new LetterScreen(stack, player, hand));
	}

	public static void copyDoorKnockSilencerPack() {
		File resourcePacksDir = new File(".", "resourcepacks");
		File target = new File(resourcePacksDir, "Furnish Door Knock Silencer.zip");
		if(!target.exists()) {
			Furnish.LOG.info("Copying Furnish Door Knock Silencer resource pack to resourcepacks.");
			try {
				resourcePacksDir.mkdirs();
				InputStream inputStream = Furnish.class.getResourceAsStream("/assets/furnish/no_door_knock.zip");
				FileOutputStream outputStream = new FileOutputStream(target);
				byte[] buf = new byte[16384];
				int len;
				while((len = inputStream.read(buf)) > 0) {
					outputStream.write(buf, 0, len);
				}
				inputStream.close();
				outputStream.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
