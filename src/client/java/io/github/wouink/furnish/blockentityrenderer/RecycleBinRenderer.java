package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.blockentity.RecycleBinBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class RecycleBinRenderer implements BlockEntityRenderer<RecycleBinBlockEntity, RecycleBinRenderState> {
    private static final float startHeight = 3.0f/16.0f;
    private static final float increment = 5.0f/16.0f;

    public RecycleBinRenderer(BlockEntityRendererProvider.Context ctx) {}

    private void prepareRenderItem(int index, PoseStack ms) {
        switch(index) {
            case 0:
                ms.translate(.35, startHeight, .35);
                break;
            case 1:
                ms.translate(.65, startHeight, .35);
                break;
            case 2:
                ms.translate(.35, startHeight, .65);
                break;
            case 3:
                ms.translate(.65, startHeight, .65);
                break;
            case 4:
                ms.translate(.35, startHeight + increment, .35);
                break;
            case 5:
                ms.translate(.65, startHeight + increment, .35);
                break;
            case 6:
                ms.translate(.35, startHeight + increment, .65);
                break;
            case 7:
                ms.translate(.65, startHeight + increment, .65);
                break;
            case 8:
                ms.translate(.5, startHeight + 2*increment, .5);
                break;
            default:
                break;
        }
        ms.scale(.6f, .6f, .6f);
    }

    @Override
    public RecycleBinRenderState createRenderState() {
        return new RecycleBinRenderState();
    }

    @Override
    public void extractRenderState(RecycleBinBlockEntity blockEntity, RecycleBinRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        int arrInd = 0;
        for(int i = 0; i < blockEntity.getContainerSize(); i++) {
            ItemStack disk = blockEntity.getItem(i);
            if(!disk.isEmpty()) {
                blockEntityRenderState.items[arrInd] = disk;
                arrInd++;
            }
        }
        for(int i = arrInd; i < blockEntityRenderState.items.length; i++)
            blockEntityRenderState.items[i] = ItemStack.EMPTY;
    }

    @Override
    public void submit(RecycleBinRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        for(int i = 0; i < blockEntityRenderState.items.length; i++) {
            if(blockEntityRenderState.items[i] != ItemStack.EMPTY) {
                prepareRenderItem(i, poseStack);
                // TODO submit item render
            }
        }
    }
}

