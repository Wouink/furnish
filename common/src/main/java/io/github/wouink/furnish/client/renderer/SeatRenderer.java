package io.github.wouink.furnish.client.renderer;

import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {

	public SeatRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public boolean shouldRender(SeatEntity entity, Frustum frustum, double d, double e, double f) {
		return false;
	}

	@Override
	public ResourceLocation getTextureLocation(SeatEntity seatEntity) {
		return null;
	}
}
