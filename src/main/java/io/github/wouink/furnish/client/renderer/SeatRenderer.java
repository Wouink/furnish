package io.github.wouink.furnish.client.renderer;

import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
	public SeatRenderer(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public ResourceLocation getTextureLocation(SeatEntity p_110775_1_) {
		return null;
	}
}
