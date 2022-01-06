package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.tileentity.ShelfTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public class ShelfRenderer extends TileEntityRenderer<ShelfTileEntity> {
	public ShelfRenderer(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ShelfTileEntity shelf, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer, int light, int overlay) {
		ItemStack stack = shelf.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			boolean blockItem = itemRenderer.getModel(stack, shelf.getLevel(), null).isGui3d();
			Direction dir = shelf.getBlockState().getValue(Plate.FACING).getOpposite();

			if(stack.getItem() instanceof BlockItem && (((BlockItem) stack.getItem()).getBlock() instanceof Plate)) prepareRenderPlate(ms, dir);
			else if(blockItem) prepareRenderBlock(ms, dir);
			else prepareRenderItem(ms, dir);

			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, light, overlay, ms, buffer);

			ms.popPose();
		}
	}

	public void prepareRenderItem(MatrixStack ms, Direction dir) {
		// center the anchor point
		switch(dir) {
			case NORTH:
				ms.translate(.5, .5, .125);
				break;
			case SOUTH:
				ms.translate(.5, .5, .875);
				break;
			case WEST:
				ms.translate(.125, .5, .5);
				break;
			default:
				ms.translate(.875, .5, .5);
		}

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));

		// scale the item
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderBlock(MatrixStack ms, Direction dir) {
		// center the anchor point
		switch(dir) {
			case NORTH:
				ms.translate(.5, .35, .15);
				break;
			case SOUTH:
				ms.translate(.5, .35, .85);
				break;
			case WEST:
				ms.translate(.15, .35, .5);
				break;
			default:
				ms.translate(.85, .35, .5);
		}

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Vector3f.YP.rotationDegrees(angle));

		// scale the block
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderPlate(MatrixStack ms, Direction dir) {
		// center the anchor point
		switch(dir) {
			case NORTH:
				ms.translate(.5, .5, .35);
				break;
			case SOUTH:
				ms.translate(.5, .5, .65);
				ms.mulPose(Vector3f.YP.rotationDegrees(180));
				break;
			case WEST:
				ms.translate(.35, .5, .5);
				ms.mulPose(Vector3f.YP.rotationDegrees(90));
				break;
			default:
				ms.translate(.65, .5, .5);
				ms.mulPose(Vector3f.YP.rotationDegrees(270));
		}

		ms.mulPose(Vector3f.XP.rotationDegrees(80));

		// scale the plate
		ms.scale(1f, 1f, 1f);
	}
}
