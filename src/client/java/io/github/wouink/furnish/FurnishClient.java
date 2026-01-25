package io.github.wouink.furnish;

import io.github.wouink.furnish.blockentityrenderer.PlateRenderer;
import io.github.wouink.furnish.blockentityrenderer.ShelfRenderer;
import io.github.wouink.furnish.blockentityrenderer.ShowcaseRenderer;
import io.github.wouink.furnish.entityrenderer.SeatEntityRenderer;
import io.github.wouink.furnish.screen.FurnitureWorkbenchScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.Block;

public class FurnishClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(FurnishContents.SEAT_ENTITY, SeatEntityRenderer::new);
		BlockEntityRenderers.register(FurnishContents.SHELF_BLOCK_ENTITY, ShelfRenderer::new);
		BlockEntityRenderers.register(FurnishContents.SHOWCASE_BLOCK_ENTITY, ShowcaseRenderer::new);
		BlockEntityRenderers.register(FurnishContents.PLATE_BLOCK_ENTITY, PlateRenderer::new);

		// https://wiki.fabricmc.net/tutorial:blockappearance
		// changing after 1.21.6
		for(Block b : FurnishContents.showcases)
			BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.translucent());

		for(Block b : FurnishContents.shutters)
			BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.translucent());

		BlockRenderLayerMap.INSTANCE.putBlock(FurnishContents.RED_BUNTING, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FurnishContents.YELLOW_BUNTING, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FurnishContents.GREEN_BUNTING, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FurnishContents.LANTERN_BUNTING, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(FurnishContents.SOUL_LANTERN_BUNTING, RenderType.translucent());

		for(ColoredSet set : FurnishContents.COLORED_SETS.values())
			BlockRenderLayerMap.INSTANCE.putBlock(set.curtain, RenderType.translucent());

		MenuScreens.register(FurnishContents.WORKBENCH_MENU, FurnitureWorkbenchScreen::new);
	}
}