package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.block.blockentity.MailboxBlockEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Matrix4f;

public class MailboxRenderer implements BlockEntityRenderer<MailboxBlockEntity> {
	private final Camera camera;
	private final Font font;

	public MailboxRenderer(BlockEntityRendererProvider.Context ctx) {
		Minecraft minecraft = Minecraft.getInstance();
		camera = minecraft.gameRenderer.getMainCamera();
		font = ctx.getFont();
	}

	@Override
	public void render(MailboxBlockEntity mailbox, float partialTicks, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
		if(shouldShowName(mailbox)) {
			renderNameTag(mailbox, ps, buffer, light);
		}
	}

	private boolean shouldShowName(MailboxBlockEntity mailbox) {
		if(!mailbox.hasOwner()) return false;

		HitResult hitResult = camera.getEntity().pick(20.0d, 0.0f, false);
		if(hitResult.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = ((BlockHitResult) hitResult).getBlockPos();
			return pos.equals(mailbox.getBlockPos());
		}

		return false;
	}

	// based on net.minecraft.client.renderer.entity.EntityRenderer#renderNameTag
	private void renderNameTag(MailboxBlockEntity mailbox, PoseStack ms, MultiBufferSource buffer, int light) {
		if(!Minecraft.renderNames()) return;

		Component content = mailbox.getOwnerDisplayName();
		if(content == null) content = Component.literal("???");

		ms.pushPose();
		ms.translate(.5, 1, .5);
		ms.mulPose(camera.rotation());
		ms.scale(-.025f, -.025f, .025f);
		Matrix4f matrix4f = ms.last().pose();
		float backOpacity = Minecraft.getInstance().options.getBackgroundOpacity(.25f);
		int alpha = (int)(backOpacity * 255.0f) << 24;
		float centerOffset = (float)(-font.width(content) / 2);
		font.drawInBatch(content, centerOffset, 0, 553648127, false, matrix4f, buffer, Font.DisplayMode.NORMAL, alpha, light);
		font.drawInBatch(content, centerOffset, 0, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, light);
		ms.popPose();
	}
}
