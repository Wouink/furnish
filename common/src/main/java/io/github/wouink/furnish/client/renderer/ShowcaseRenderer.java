package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Plate;
import io.github.wouink.furnish.block.tileentity.ShowcaseTileEntity;
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

public class ShowcaseRenderer implements BlockEntityRenderer<ShowcaseTileEntity> {

	private final ItemRenderer itemRenderer;

	public ShowcaseRenderer(BlockEntityRendererProvider.Context ctx) {
		Minecraft minecraft = Minecraft.getInstance();
		itemRenderer = minecraft.getItemRenderer();
	}

	@Override
	public void render(ShowcaseTileEntity showcase, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = showcase.getHeldItem();
		if(!stack.isEmpty()) {
			ms.pushPose();

			Direction dir = showcase.getBlockState().getValue(Plate.FACING).getOpposite();
			boolean powered = showcase.getBlockState().getValue(BlockStateProperties.POWERED).booleanValue();
			BakedModel model = itemRenderer.getModel(stack, showcase.getLevel(), null, 0);
			float angle = powered ? showcase.getLevel().getGameTime() % 360 : 0;

			if(model.isGui3d()) prepareRenderBlock(ms, dir, angle);
			else prepareRenderItem(ms, dir, angle);

			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ms, buffer, light, overlay, model);

			ms.popPose();
		}
	}

	public void prepareRenderBlock(PoseStack ms, Direction dir, float angleOffset) {
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
		ms.mulPose(Axis.YP.rotationDegrees(angleOffset));

		// scale the block
		ms.scale(.8f, .8f, .8f);
	}

	public void prepareRenderItem(PoseStack ms, Direction dir, float angleOffset) {
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
		ms.mulPose(Axis.YP.rotationDegrees(angleOffset));

		// slightly lean the item
		ms.mulPose(Axis.XP.rotationDegrees(10));

		// scale the plate
		ms.scale(1f, 1f, 1f);
	}
}
