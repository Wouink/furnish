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
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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
		// todo not working on forge anymore
		MenuRegistry.registerScreenFactory(FurnishRegistries.Furniture_Workbench_Container.get(), FurnitureWorkbenchScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Crate_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Mailbox_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Bookshelf_Chest_Container.get(), ConditionalSlotContainerScreen::new);
		MenuRegistry.registerScreenFactory(FurnishRegistries.Disk_Rack_Container.get(), DiskRackScreen::new);
		Furnish.LOG.info("Bound Furnish Containers to their Screens.");
	}

	public static void registerEntityRenderers() {
		// todo none of that works
		EntityRendererRegistry.register(FurnishRegistries.Seat_Entity::get, SeatRenderer::new);
		Furnish.LOG.info("Registered Furnish Entities Renderers.");

		BlockEntityRendererRegistry.register(FurnishRegistries.Mailbox_BlockEntity.get(), MailboxRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Plate_BlockEntity.get(), PlateRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Shelf_BlockEntity.get(), ShelfRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Showcase_BlockEntity.get(), ShowcaseRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Disk_Rack_BlockEntity.get(), DiskRackRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Recycle_Bin_BlockEntity.get(), RecycleBinRenderer::new);
		BlockEntityRendererRegistry.register(FurnishRegistries.Flower_Pot_BlockEntity.get(), FlowerPotRenderer::new);
		Furnish.LOG.info("Registered Furnish Blocks Entities Renderers.");
	}

	public static void openLetterGui(ItemStack stack, Player player, InteractionHand hand) {
		// todo everything is blurred and no text input
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

	/**
	 * checks if there is snow above a leave, or above the leave above (recursively)
	 * @param level
	 * @param pos
	 * @param includeAir should we continue checking if we encounter air, such as in a spruce tree?
	 * @param airBelow is the block below already air? we only allow a 1 block gap in-between leaves, for spruce trees
	 * @return is the leave considered "snowy"
	 */
	private static boolean isLeaveSnowy(BlockAndTintGetter level, BlockPos pos, boolean includeAir, boolean airBelow) {
		if(level.getBlockState(pos.above()).is(Blocks.SNOW)) return true;
		else if(level.getBlockState(pos.above()).is(BlockTags.LEAVES)) return isLeaveSnowy(level, pos.above(), includeAir, false);
		else if(includeAir && !airBelow && level.getBlockState(pos.above()).isAir()) return isLeaveSnowy(level, pos.above(), true, true);
		return false;
	}

	// White tint for Snowy Leaves
	// todo When Furnish will be ported to Architectury API, replace this event with
	// ColorHandlerRegistry.registerBlockColors in client setup
	/*
	@SubscribeEvent
	public static void onBlockColorEvent(RegisterColorHandlersEvent.Block event) {
		event.register((state, level, pos, index) -> {
					if(level == null) return FoliageColor.getDefaultColor();
					boolean spruce = state.getBlock() == Blocks.SPRUCE_LEAVES;

					int color = BiomeColors.getAverageFoliageColor(level, pos);
					if(state.getBlock() == Blocks.BIRCH_LEAVES) color = FoliageColor.getBirchColor();
					else if(spruce) color = FoliageColor.getEvergreenColor();

					if(!Furnish.CONFIG.whitenSnowyLeaves.get()) return color;
					else if(spruce && !Furnish.CONFIG.whitenSpruceLeaves.get()) return color;

					if(!isLeaveSnowy(level, pos, spruce, false)) return color;

					int red = (color >> 16 & 0xff);
					int green = (color >> 8 & 0xff);
					int blue = (color & 0xff);
					red += (0xff - red);
					green += (0xff - green);
					blue += (0xff - blue);

					return red << 16 | green << 8 | blue;
				}, Blocks.OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES,
				Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES);
	}
	 */
}
