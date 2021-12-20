package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.wouink.furnish.block.tileentity.MailboxTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class MailboxRenderer extends TileEntityRenderer<MailboxTileEntity> {
	public MailboxRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(MailboxTileEntity mailbox, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		if(shouldShowName(mailbox)) {
			renderNameTag(mailbox, ms, buffer, light);
		}
	}

	private boolean shouldShowName(MailboxTileEntity mailbox) {
		if(!mailbox.hasOwner()) return false;
		if(renderer.cameraHitResult.getType() == RayTraceResult.Type.BLOCK) {
			BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) renderer.cameraHitResult;
			return blockRayTraceResult.getBlockPos().equals(mailbox.getBlockPos());
		}
		return false;
	}

	// based on net.minecraft.client.renderer.entity.EntityRenderer#renderNameTag
	private void renderNameTag(MailboxTileEntity mailbox, MatrixStack ms, IRenderTypeBuffer buffer, int light) {
		if(!Minecraft.renderNames()) return;

		ITextComponent content = mailbox.getOwnerDisplayName();
		if(content == null) content = new StringTextComponent("???");

		ms.pushPose();
		ms.translate(.5, 1, .5);
		ms.mulPose(this.renderer.camera.rotation());
		ms.scale(-.025f, -.025f, .025f);
		Matrix4f matrix4f = ms.last().pose();
		float backOpacity = Minecraft.getInstance().options.getBackgroundOpacity(.25f);
		int alpha = (int)(backOpacity * 255.0f) << 24;
		FontRenderer fontRenderer = this.renderer.getFont();
		float centerOffset = (float)(-fontRenderer.width(content) / 2);
		fontRenderer.drawInBatch(content, centerOffset, 0, 553648127, false, matrix4f, buffer, false, alpha, light);
		fontRenderer.drawInBatch(content, centerOffset, 0, -1, false, matrix4f, buffer, false, 0, light);
		ms.popPose();
	}
}
