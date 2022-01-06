package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.tileentity.PlateTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class PlateRenderer extends TileEntityRenderer<PlateTileEntity> {
	private static final double ITEM_HEIGHT = 1 / 16.0d;

	public PlateRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(PlateTileEntity plate, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = plate.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			boolean blockItem = itemRenderer.getModel(stack, plate.getLevel(), null).isGui3d();
			Direction dir = plate.getBlockState().getValue(Plate.FACING).getOpposite();

			if(blockItem) prepareRenderBlock(ms, dir);
			else prepareRenderItem(ms, dir);

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, light, overlay, ms, buffer);

			ms.popPose();
		}
	}

	public void prepareRenderItem(MatrixStack ms, Direction dir) {
		// center the anchor point
		ms.translate(0.5d, 0.08, 0.5);

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));

		// place the item flat
		ms.mulPose(Vector3f.XP.rotationDegrees(90));

		// scale the item
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderBlock(MatrixStack ms, Direction dir) {
		// center the anchor point
		ms.translate(.5, .23, .5);

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));

		// scale the block
		ms.scale(.8f, .8f, .8f);
	}
}
