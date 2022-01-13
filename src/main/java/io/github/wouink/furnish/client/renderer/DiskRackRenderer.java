package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.wouink.furnish.block.tileentity.DiskRackTileEntity;
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

public class DiskRackRenderer extends TileEntityRenderer<DiskRackTileEntity> {
	public DiskRackRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(DiskRackTileEntity rack, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		if(rack.isEmpty()) return;

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		Direction dir = rack.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);

		for(int i = 0; i < rack.getContainerSize(); i++) {
			ItemStack stack = rack.getItemsForRender().get(i);
			if(!stack.isEmpty()) {
				ms.pushPose();
				prepareRenderItem(i, dir, ms);
				itemRenderer.renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, light, overlay, ms, buffer);
				ms.popPose();
			}
		}
	}

	private void prepareRenderItem(int index, Direction dir, MatrixStack ms) {
		ms.mulPose(Vector3f.YP.rotationDegrees(dir.toYRot()));
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
