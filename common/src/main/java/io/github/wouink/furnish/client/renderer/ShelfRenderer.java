package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.blockentity.ShelfBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {

	private final ItemRenderer itemRenderer;

	public ShelfRenderer(BlockEntityRendererProvider.Context ctx) {
		itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(ShelfBlockEntity shelf, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = shelf.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			BakedModel model = itemRenderer.getModel(stack, shelf.getLevel(), null, 0);
			Direction dir = shelf.getBlockState().getValue(Plate.FACING).getOpposite();

			if(stack.getItem() instanceof BlockItem && (((BlockItem) stack.getItem()).getBlock() instanceof Plate)) prepareRenderPlate(ms, dir);
			else if(model.isGui3d()) prepareRenderBlock(ms, dir);
			else prepareRenderItem(ms, dir);

			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ms, buffer, light, overlay, model);

			ms.popPose();
		}
	}

	public void prepareRenderItem(PoseStack ms, Direction dir) {
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
		ms.mulPose(Axis.YP.rotationDegrees(angle));

		// scale the item
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderBlock(PoseStack ms, Direction dir) {
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
		ms.mulPose(Axis.YP.rotationDegrees(angle));

		// scale the block
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderPlate(PoseStack ms, Direction dir) {
		// center the anchor point
		switch(dir) {
			case NORTH:
				ms.translate(.5, .5, .35);
				break;
			case SOUTH:
				ms.translate(.5, .5, .65);
				ms.mulPose(Axis.YP.rotationDegrees(180));
				break;
			case WEST:
				ms.translate(.35, .5, .5);
				ms.mulPose(Axis.YP.rotationDegrees(90));
				break;
			default:
				ms.translate(.65, .5, .5);
				ms.mulPose(Axis.YP.rotationDegrees(270));
		}

		ms.mulPose(Axis.XP.rotationDegrees(80));

		// scale the plate
		ms.scale(1f, 1f, 1f);
	}
}
