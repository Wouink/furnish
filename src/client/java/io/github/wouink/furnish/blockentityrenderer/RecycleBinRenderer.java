package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.blockentity.RecycleBinBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RecycleBinRenderer implements BlockEntityRenderer<RecycleBinBlockEntity> {

    private final ItemRenderer itemRenderer;
    private static final float startHeight = 3.0f/16.0f;
    private static final float increment = 5.0f/16.0f;

    public RecycleBinRenderer(BlockEntityRendererProvider.Context ctx) {
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(final RecycleBinBlockEntity recycleBin, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if(recycleBin.getBlockState().getBlock() == FurnishContents.TRASH_CAN) return;
        int itemIndex = 0;
        for(int slot = 0; slot < recycleBin.getContainerSize(); slot++) {
            ItemStack stack = recycleBin.getItem(slot);
            if(!stack.isEmpty()) {
                ms.pushPose();
                BakedModel model = itemRenderer.getModel(stack, recycleBin.getLevel(), null, 0);
                prepareRenderItem(itemIndex, ms);
                itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ms, buffer, light, overlay, model);
                ms.popPose();
                itemIndex++;
            }
        }
    }

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
}

