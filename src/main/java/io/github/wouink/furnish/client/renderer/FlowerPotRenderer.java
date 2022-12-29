package io.github.wouink.furnish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import io.github.wouink.furnish.block.FlowerPot;
import io.github.wouink.furnish.block.tileentity.FlowerPotTileEntity;
import io.github.wouink.furnish.block.util.VectorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class FlowerPotRenderer implements BlockEntityRenderer<FlowerPotTileEntity> {
	private final ItemRenderer itemRenderer;

	public FlowerPotRenderer(BlockEntityRendererProvider.Context ctx) {
		itemRenderer = Minecraft.getInstance().getItemRenderer();
	}

	@Override
	public void render(FlowerPotTileEntity flowerPot, float partialTicks, PoseStack ps, MultiBufferSource buffers, int combinedLight, int overlay) {
		// System.out.println("rendering");
		// Direction dir = flowerPot.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
		Direction dir = Direction.EAST;
		ps.mulPose(Vector3f.YP.rotationDegrees(dir.toYRot()));

		for(int i = 0; i < flowerPot.getContainerSize(); i++) {
			// System.out.println("checking item in slot " + i);
			ItemStack flower = flowerPot.getItemsForRender().get(i);
			if(!flower.isEmpty()) {
				System.out.println("flower is not empty");
				ps.pushPose();
				Vector3d flowerRenderPos = VectorHelper.rotateVectorForRenderer(((FlowerPot)flowerPot.getBlockState().getBlock()).getRenderPos(i), dir);
				BakedModel bakedModel = itemRenderer.getModel(flower, flowerPot.getLevel(), null, 0);
				itemRenderer.render(flower, ItemTransforms.TransformType.FIXED, true, ps, buffers, combinedLight, overlay, bakedModel);
				ps.popPose();
			}
		}
	}
}
