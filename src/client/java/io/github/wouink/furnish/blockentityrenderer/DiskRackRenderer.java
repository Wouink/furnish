package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.DiskRack;
import io.github.wouink.furnish.blockentity.DiskRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DiskRackRenderer implements BlockEntityRenderer<DiskRackBlockEntity, DiskRackRenderState> {

    private ItemModelResolver itemModelResolver;

    public DiskRackRenderer(BlockEntityRendererProvider.Context ctx) {
        itemModelResolver = ctx.itemModelResolver();
    }

    private void prepareRenderItem(int index, Direction dir, PoseStack ms) {
        ms.mulPose(Axis.YP.rotationDegrees(dir.toYRot()));
        // offset = index * 2/16b + (.5*1/16bl for centering)
        double offset = index * .125 + .03125;
        switch(dir) {
            case SOUTH:
                ms.translate(.5, .2, 1.0 - offset);
                break;
            case NORTH:
                ms.translate(-.5, .2, -offset);
                break;
            case WEST:
                ms.translate(-.5, .2, offset);
                break;
            default:
                ms.translate(.5, .2, offset - 1.0);
                break;
        }
        ms.scale(.6f, .6f, .6f);
    }

    @Override
    public DiskRackRenderState createRenderState() {
        return new DiskRackRenderState();
    }

    @Override
    public void extractRenderState(DiskRackBlockEntity blockEntity, DiskRackRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        blockEntityRenderState.facing = blockEntity.getBlockState().getValue(DiskRack.FACING).getOpposite();

        int j = 0;
        for(int i = blockEntity.getContainerSize() - 1; i >= 0; i--) {
            ItemStack disk = blockEntity.getItem(i);
            itemModelResolver.updateForTopItem(blockEntityRenderState.disks[j], disk, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, j);
            j++;
        }
    }

    @Override
    public void submit(DiskRackRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        int light = 15728880;
        int outlineColor = 0;

        for(int i = 0; i < blockEntityRenderState.disks.length; i++) {
            poseStack.pushPose();
            prepareRenderItem(i, blockEntityRenderState.facing, poseStack);
            blockEntityRenderState.disks[i].submit(poseStack, submitNodeCollector, light, OverlayTexture.NO_OVERLAY, outlineColor);
            poseStack.popPose();
        }
    }
}
