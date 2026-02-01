package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.blockentity.DiskRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DiskRackRenderer implements BlockEntityRenderer<DiskRackBlockEntity> {

    private final ItemRenderer itemRenderer;

    public DiskRackRenderer(BlockEntityRendererProvider.Context ctx) {
        Minecraft minecraft = Minecraft.getInstance();
        itemRenderer = minecraft.getItemRenderer();
    }

    @Override
    public void render(DiskRackBlockEntity rack, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if(rack.isEmpty()) return;

        Direction dir = rack.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

        for(int i = 0; i < rack.getContainerSize(); i++) {
            ItemStack stack = rack.getItem(i);
            if(!stack.isEmpty()) {
                ms.pushPose();
                prepareRenderItem(i, dir, ms);
                BakedModel model = itemRenderer.getModel(stack, rack.getLevel(), null, 0);
                itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ms, buffer, light, overlay, model);
                ms.popPose();
            }
        }
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
}
