package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Shelf;
import io.github.wouink.furnish.blockentity.PlateBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.Vec3;

public class PlateRenderer implements BlockEntityRenderer<PlateBlockEntity, StackHoldingRenderState> {

    public PlateRenderer(BlockEntityRendererProvider.Context ctx) {}

    public void prepareRenderItem(PoseStack ms, Direction dir) {
        // center the anchor point
        ms.translate(0.5d, 0.08, 0.5);

        // rotate to the adequate direction
        float angle = -dir.toYRot();
        ms.mulPose(Axis.YP.rotationDegrees(angle));

        // place the item flat
        ms.mulPose(Axis.XP.rotationDegrees(90));

        // scale the item
        ms.scale(.6f, .6f, .6f);
    }

    public void prepareRenderBlock(PoseStack ms, Direction dir) {
        // center the anchor point
        ms.translate(.5, .23, .5);

        // rotate to the adequate direction
        float angle = -dir.toYRot();
        ms.mulPose(Axis.YP.rotationDegrees(angle));

        // scale the block
        ms.scale(.8f, .8f, .8f);
    }

    @Override
    public void extractRenderState(PlateBlockEntity blockEntity, StackHoldingRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        blockEntityRenderState.heldItem = blockEntity.getHeldItem();
        blockEntityRenderState.facing = blockEntity.getBlockState().getValue(Shelf.FACING);
    }

    @Override
    public StackHoldingRenderState createRenderState() {
        return new StackHoldingRenderState();
    }

    @Override
    public void submit(StackHoldingRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(!blockEntityRenderState.heldItem.isEmpty()) {
            poseStack.pushPose();

            // model.isGui3D was better
            if(blockEntityRenderState.heldItem.getItem() instanceof BlockItem)
                prepareRenderBlock(poseStack, blockEntityRenderState.facing);
            else prepareRenderItem(poseStack, blockEntityRenderState.facing);

            int light = 15728880;
            int outlineColor = 0;
            blockEntityRenderState.item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, outlineColor);

            poseStack.popPose();
        }
    }
}
