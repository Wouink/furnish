package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.blockentity.PlateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PlateRenderer implements BlockEntityRenderer<PlateBlockEntity> {
	private final ItemRenderer itemRenderer;

	public PlateRenderer(BlockEntityRendererProvider.Context ctx) {
		Minecraft minecraft = Minecraft.getInstance();
		itemRenderer = minecraft.getItemRenderer();
	}

	@Override
	public void render(PlateBlockEntity plate, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = plate.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			BakedModel model = itemRenderer.getModel(stack, plate.getLevel(), null, 0);
			Direction dir = plate.getBlockState().getValue(Plate.FACING).getOpposite();

			if(model.isGui3d()) prepareRenderBlock(ms, dir);
			else prepareRenderItem(ms, dir);

			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ms, buffer, light, overlay, model);

			ms.popPose();
		}
	}

	public void prepareRenderItem(PoseStack ms, Direction dir) {
		// center the anchor point
		ms.translate(0.5d, 0.08, 0.5);

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Axis.YP.rotationDegrees(angle));

		// place the item flat
		ms.mulPose(Axis.XP.rotationDegrees(90));

		// scale the item
		ms.scale(.6f, .6f, .6f);
	}

	public void prepareRenderBlock(PoseStack ms, Direction dir) {
		// center the anchor point
		ms.translate(.5, .23, .5);

		// rotate to the adequate direction
		float angle = -dir.toYRot();
		ms.mulPose(Axis.YP.rotationDegrees(angle));

		// scale the block
		ms.scale(.8f, .8f, .8f);
	}
}
