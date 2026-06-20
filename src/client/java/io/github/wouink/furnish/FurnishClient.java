package io.github.wouink.furnish;

import io.github.wouink.furnish.blockentityrenderer.*;
import io.github.wouink.furnish.entityrenderer.SeatEntityRenderer;
import io.github.wouink.furnish.network.OpenItemGUIS2C;
import io.github.wouink.furnish.screen.DiskRackScreen;
import io.github.wouink.furnish.screen.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.screen.LetterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FurnishClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(FurnishContents.SEAT_ENTITY, SeatEntityRenderer::new);
		BlockEntityRenderers.register(FurnishContents.SHELF_BLOCK_ENTITY, ShelfRenderer::new);
		BlockEntityRenderers.register(FurnishContents.SHOWCASE_BLOCK_ENTITY, ShowcaseRenderer::new);
		BlockEntityRenderers.register(FurnishContents.PLATE_BLOCK_ENTITY, PlateRenderer::new);
		BlockEntityRenderers.register(FurnishContents.MAILBOX_BLOCK_ENTITY, MailboxRenderer::new);
		BlockEntityRenderers.register(FurnishContents.DISK_RACK_BLOCK_ENTITY, DiskRackRenderer::new);
		BlockEntityRenderers.register(FurnishContents.RECYCLE_BIN_BLOCK_ENTITY, RecycleBinRenderer::new);

		// making blocks translucent no longer needed in 26.1+, it is handled automatically
		// https://github.com/neoforged/.github/blob/main/primers/26.1/index.md#materials-and-dynamic-layer-selection

		MenuScreens.register(FurnishContents.WORKBENCH_MENU, FurnitureWorkbenchScreen::new);
		MenuScreens.register(FurnishContents.DISK_RACK_MENU, DiskRackScreen::new);

		ClientPlayNetworking.registerGlobalReceiver(OpenItemGUIS2C.TYPE, (message, context) -> {
			context.client().execute(() -> {
				if(message instanceof OpenItemGUIS2C request) {
					ItemStack requester = request.source();
					if(requester.is(FurnishContents.LETTER)) {
						Furnish.LOGGER.debug("Open Letter GUI requested for slot " + request.slot());
						Minecraft.getInstance().gui.setScreen(new LetterScreen(requester, context.player(), request.slot()));
					}
				}
			});
		});
	}
}