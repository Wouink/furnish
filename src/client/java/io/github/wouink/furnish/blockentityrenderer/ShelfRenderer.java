package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.Shelf;
import io.github.wouink.furnish.blockentity.ShelfBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.Vec3;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity, StackHoldingRenderState> {

    public ShelfRenderer(BlockEntityRendererProvider.Context ctx) {}

    public void prepareRenderItem(PoseStack ms, Direction dir) {
        // center the anchor point
        switch(dir) {
            case NORTH:
                ms.translate(.5, .5, .125);
                break;
            case SOUTH:
                ms.translate(.5, .5, .875);
                break;
            case WEST:
                ms.translate(.125, .5, .5);
                break;
            default:
                ms.translate(.875, .5, .5);
        }

        // rotate to the adequate direction
        float angle = -dir.toYRot();
        ms.mulPose(Axis.YP.rotationDegrees(angle));

        // scale the item
        ms.scale(.6f, .6f, .6f);
    }

    public void prepareRenderBlock(PoseStack ms, Direction dir) {
        // center the anchor point
        switch(dir) {
            case NORTH:
                ms.translate(.5, .35, .15);
                break;
            case SOUTH:
                ms.translate(.5, .35, .85);
                break;
            case WEST:
                ms.translate(.15, .35, .5);
                break;
            default:
                ms.translate(.85, .35, .5);
        }

        // rotate to the adequate direction
        float angle = -dir.toYRot();
        ms.mulPose(Axis.YP.rotationDegrees(angle));

        // scale the block
        ms.scale(.6f, .6f, .6f);
    }

    public void prepareRenderPlate(PoseStack ms, Direction dir) {
        // center the anchor point
        switch(dir) {
            case NORTH:
                ms.translate(.5, .5, .35);
                break;
            case SOUTH:
                ms.translate(.5, .5, .65);
                ms.mulPose(Axis.YP.rotationDegrees(180));
                break;
            case WEST:
                ms.translate(.35, .5, .5);
                ms.mulPose(Axis.YP.rotationDegrees(90));
                break;
            default:
                ms.translate(.65, .5, .5);
                ms.mulPose(Axis.YP.rotationDegrees(270));
        }

        ms.mulPose(Axis.XP.rotationDegrees(80));

        // scale the plate
        ms.scale(1f, 1f, 1f);
    }

    @Override
    public void extractRenderState(ShelfBlockEntity blockEntity, StackHoldingRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
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
            if(blockEntityRenderState.heldItem.getItem() instanceof BlockItem blockItem) {
                if(blockItem.getBlock() instanceof Plate) prepareRenderPlate(poseStack, blockEntityRenderState.facing);
                else prepareRenderBlock(poseStack, blockEntityRenderState.facing);
            }
            else prepareRenderItem(poseStack, blockEntityRenderState.facing);

            int light = 15728880;
            int outlineColor = 0;
            blockEntityRenderState.item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, outlineColor);

            poseStack.popPose();
        }
    }
}
