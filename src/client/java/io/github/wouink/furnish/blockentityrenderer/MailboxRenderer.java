package io.github.wouink.furnish.blockentityrenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.blockentity.MailboxBlockEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class MailboxRenderer implements BlockEntityRenderer<MailboxBlockEntity, MailboxRenderState> {
    private final Camera camera;
    private final Font font;

    public MailboxRenderer(BlockEntityRendererProvider.Context ctx) {
        Minecraft minecraft = Minecraft.getInstance();
        camera = minecraft.gameRenderer.getMainCamera();
        font = ctx.font();
    }

    private boolean shouldShowName(MailboxBlockEntity mailbox) {
        if(!mailbox.hasOwner()) return false;

        HitResult hitResult = camera.entity().pick(20.0d, 0.0f, false);
        if(hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
            return pos.equals(mailbox.getBlockPos());
        }

        return false;
    }

    @Override
    public MailboxRenderState createRenderState() {
        return new MailboxRenderState();
    }

    @Override
    public void extractRenderState(MailboxBlockEntity blockEntity, MailboxRenderState blockEntityRenderState, float f, Vec3 vec3, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, blockEntityRenderState, f, vec3, crumblingOverlay);
        blockEntityRenderState.shouldRender = shouldShowName(blockEntity);
        blockEntityRenderState.ownerDisplayName = blockEntity.getOwnerDisplayName();
        if(blockEntityRenderState.ownerDisplayName == null) blockEntityRenderState.ownerDisplayName = Component.literal("???");
    }

    @Override
    public void submit(MailboxRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if(Minecraft.renderNames() && blockEntityRenderState.shouldRender) {
            // see on net.minecraft.client.renderer.entity.EntityRenderer#renderNameTag
        }
    }
}
