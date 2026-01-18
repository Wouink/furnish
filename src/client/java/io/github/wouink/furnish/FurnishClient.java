package io.github.wouink.furnish;

import io.github.wouink.furnish.entityrenderer.SeatEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class FurnishClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(FurnishContents.SEAT_ENTITY, SeatEntityRenderer::new);
	}
}