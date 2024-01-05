package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.wouink.furnish.block.Showcase;
import io.github.wouink.furnish.block.blockentity.ShowcaseBlockEntity;
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

public class ShowcaseRenderer implements BlockEntityRenderer<ShowcaseBlockEntity> {

	private final ItemRenderer itemRenderer;

	public ShowcaseRenderer(BlockEntityRendererProvider.Context ctx) {
		Minecraft minecraft = Minecraft.getInstance();
		itemRenderer = minecraft.getItemRenderer();
	}

	@Override
	public void render(ShowcaseBlockEntity showcase, float partialTicks, PoseStack ps, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = showcase.getHeldItem();
		if(!stack.isEmpty()) {
			ps.pushPose();

			Direction dir = showcase.getBlockState().getValue(Showcase.FACING).getOpposite();
			boolean powered = showcase.getBlockState().getValue(BlockStateProperties.POWERED).booleanValue();
			BakedModel model = itemRenderer.getModel(stack, showcase.getLevel(), null, 0);

			// if the block is powered, the item will be rotating
			// the current rotation angle is calculated using the game time
			float angle = powered ? showcase.getLevel().getGameTime() % 360 : 0;

			if(model.isGui3d()) prepareRenderBlock(ps, dir, angle);
			else prepareRenderItem(ps, dir, angle);

			itemRenderer.render(stack, ItemDisplayContext.FIXED, true, ps, buffer, light, overlay, model);

			ps.popPose();
		}
	}

	public void prepareRenderBlock(PoseStack ps, Direction dir, float angleOffset) {
		// center the anchor point
		ps.translate(.5, .4, .5);

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
		ps.mulPose(Axis.YP.rotationDegrees(angleOffset));

		// scale the block
		ps.scale(.8f, .8f, .8f);
	}

	public void prepareRenderItem(PoseStack ps, Direction dir, float angleOffset) {
		dir = dir.getOpposite();

		// center the anchor point
		ps.translate(.5, .4, .5);

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
		ps.mulPose(Axis.YP.rotationDegrees(angleOffset));

		// slightly lean the item
		ps.mulPose(Axis.XP.rotationDegrees(10));

		// scale the item
		ps.scale(1f, 1f, 1f);
	}
}
