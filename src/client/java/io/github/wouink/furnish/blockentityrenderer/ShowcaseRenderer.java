package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Showcase;
import io.github.wouink.furnish.blockentity.ShowcaseBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;

public class ShowcaseRenderer implements BlockEntityRenderer<ShowcaseBlockEntity, ShowcaseRenderState> {
    private final ItemModelResolver itemModelResolver;

    public ShowcaseRenderer(BlockEntityRendererProvider.Context ctx) {
        Minecraft minecraft = Minecraft.getInstance();
        itemModelResolver = minecraft.getItemModelResolver();
    }

    public void prepareRenderBlock(PoseStack ps, Direction dir, float angleOffset) {
        // center the anchor point
        ps.translate(.5, .4, .5);

        // rotate
        switch(dir) {
            case NORTH:
                break;
            case SOUTH:
                angleOffset += 180;
                break;
            case WEST:
                angleOffset += 90;
                break;
            default:
                angleOffset += 270;
        }
        ps.mulPose(Axis.YP.rotationDegrees(angleOffset));

        // scale the block
        ps.scale(.8f, .8f, .8f);
    }

    public void prepareRenderItem(PoseStack ps, Direction dir, float angleOffset) {
        dir = dir.getOpposite();

        // center the anchor point
        ps.translate(.5, .4, .5);

        // rotate
        switch(dir) {
            case NORTH:
                break;
            case SOUTH:
                angleOffset += 180;
                break;
            case WEST:
                angleOffset += 90;
                break;
            default:
                angleOffset += 270;
        }
        ps.mulPose(Axis.YP.rotationDegrees(angleOffset));

        // slightly lean the item
        ps.mulPose(Axis.XP.rotationDegrees(10));

        // scale the item
        ps.scale(1f, 1f, 1f);
    }

    @Override
    public void extractRenderState(ShowcaseBlockEntity blockEntity, ShowcaseRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);

        blockEntityRenderState.facing = blockEntity.getBlockState().getValue(Showcase.FACING);
        blockEntityRenderState.heldItem = blockEntity.getHeldItem();
        // not sure
        itemModelResolver.updateForTopItem(blockEntityRenderState.item, blockEntity.getHeldItem(), ItemDisplayContext.FIXED, null, null, 0);

        boolean powered = blockEntity.getBlockState().getValue(Showcase.POWERED);
        // rotate the item if the showcase is powered
        blockEntityRenderState.angle = powered ? blockEntity.getLevel().getGameTime() % 360 : 0;
    }

    @Override
    public ShowcaseRenderState createRenderState() {
        return new ShowcaseRenderState();
    }

    @Override
    public void submit(ShowcaseRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(!blockEntityRenderState.heldItem.isEmpty()) {
            poseStack.pushPose();

            // model.isGui3D was better
            if(blockEntityRenderState.heldItem.getItem() instanceof BlockItem)
                prepareRenderBlock(poseStack, blockEntityRenderState.facing, blockEntityRenderState.angle);
            else prepareRenderItem(poseStack, blockEntityRenderState.facing, blockEntityRenderState.angle);

            int light = 15728880;
            int outlineColor = 0;
            blockEntityRenderState.item.submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, outlineColor);

            poseStack.popPose();
        }
    }
}
