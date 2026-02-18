package io.github.wouink.furnish.entityrenderer;

import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SeatEntityRenderer extends EntityRenderer<SeatEntity, SeatRenderState> {
    public SeatEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(SeatEntity entity, Frustum frustum, double d, double e, double f) {
        return false;
    }

    @Override
    public SeatRenderState createRenderState() {
        return new SeatRenderState();
    }
}
