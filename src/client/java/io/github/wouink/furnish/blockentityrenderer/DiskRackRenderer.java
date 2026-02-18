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
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DiskRackRenderer implements BlockEntityRenderer<DiskRackBlockEntity, DiskRackRenderState> {

    private final ItemRenderer itemRenderer;

    public DiskRackRenderer(BlockEntityRendererProvider.Context ctx) {
        Minecraft minecraft = Minecraft.getInstance();
        itemRenderer = minecraft.getItemRenderer();
    }

    // TODO disks are not exactly centered
    private void prepareRenderItem(int index, Direction dir, PoseStack ms) {
        ms.mulPose(Axis.YP.rotationDegrees(dir.toYRot()));
        // offset = 1/16bl + index * 2/16b + (.5*1/16bl for centering)
        double offset = .0625 + index * .125 + .03125;
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
        blockEntityRenderState.facing = blockEntity.getBlockState().getValue(DiskRack.FACING);
        int arrInd = 0;
        for(int i = 0; i < blockEntity.getContainerSize(); i++) {
            ItemStack disk = blockEntity.getItem(i);
            if(!disk.isEmpty()) {
                blockEntityRenderState.disks[arrInd] = disk;
                arrInd++;
            }
        }
        for(int i = arrInd; i < blockEntityRenderState.disks.length; i++)
            blockEntityRenderState.disks[i] = ItemStack.EMPTY;
    }

    @Override
    public void submit(DiskRackRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        for(int i = 0; i < blockEntityRenderState.disks.length; i++) {
            if(blockEntityRenderState.disks[i] != ItemStack.EMPTY) {
                prepareRenderItem(i, blockEntityRenderState.facing, poseStack);
                // TODO submit item renderers
            }
        }
    }
}
