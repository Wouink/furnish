package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.tileentity.ShowcaseTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class ShowcaseRenderer extends TileEntityRenderer<ShowcaseTileEntity> {
	public ShowcaseRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ShowcaseTileEntity showcase, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = showcase.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			Direction dir = showcase.getBlockState().getValue(Plate.FACING).getOpposite();
			boolean powered = showcase.getBlockState().getValue(BlockStateProperties.POWERED).booleanValue();
			boolean blockItem = itemRenderer.getModel(stack, showcase.getLevel(), null).isGui3d();
			float angle = powered ? showcase.getLevel().getGameTime() % 360 : 0;

			if(blockItem) prepareRenderBlock(ms, dir, angle);
			else prepareRenderItem(ms, dir, angle);

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, light, overlay, ms, buffer);

			ms.popPose();
		}
	}

	public void prepareRenderBlock(MatrixStack ms, Direction dir, float angleOffset) {
		// center the anchor point
		ms.translate(.5, .4, .5);

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
		ms.mulPose(Vector3f.YP.rotationDegrees(angleOffset));

		// scale the block
		ms.scale(.8f, .8f, .8f);
	}

	public void prepareRenderItem(MatrixStack ms, Direction dir, float angleOffset) {
		dir = dir.getOpposite();

		// center the anchor point
		ms.translate(.5, .4, .5);

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
		ms.mulPose(Vector3f.YP.rotationDegrees(angleOffset));

		// slightly lean the item
		ms.mulPose(Vector3f.XP.rotationDegrees(10));

		// scale the plate
		ms.scale(1f, 1f, 1f);
	}
}
