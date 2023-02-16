package io.github.wouink.furnish.setup;

import io.github.wouink.furnish.Furnish;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FurnishClient {

	public static void clientSetup() {
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Red_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Yellow_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Green_Bunting.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Jungle_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Acacia_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Crimson_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Warped_Shutter.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Recycle_Bin.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Iron_Bars_Top.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Iron_Gate.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Cobweb.get(), RenderType.translucent());
		// ItemBlockRenderTypes.setRenderLayer(Flower_Pot.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FurnishBlocks.Snow_On_Stairs.get(), RenderType.translucent());
		// ItemBlockRenderTypes.setRenderLayer(Paper_Sheet.get(), RenderType.translucent());
		for(RegistryObject<Block> b : FurnishBlocks.Showcases) ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.translucent());
	}

	/**
	 * checks if there is snow above a leave, or above the leave above (recursively)
	 * @param level
	 * @param pos
	 * @param includeAir should we continue checking if we encounter air, such as in a spruce tree?
	 * @param airBelow is the block below already air? we only allow a 1 block gap in-between leaves, for spruce trees
	 * @return is the leave considered "snowy"
	 */
	private static boolean isLeaveSnowy(BlockAndTintGetter level, BlockPos pos, boolean includeAir, boolean airBelow) {
		if(level.getBlockState(pos.above()).is(Blocks.SNOW)) return true;
		else if(level.getBlockState(pos.above()).is(BlockTags.LEAVES)) return isLeaveSnowy(level, pos.above(), includeAir, false);
		else if(includeAir && !airBelow && level.getBlockState(pos.above()).isAir()) return isLeaveSnowy(level, pos.above(), true, true);
		return false;
	}

	// White tint for Snowy Leaves
	// todo When Furnish will be ported to Architectury API, replace this event with
	// ColorHandlerRegistry.registerBlockColors in client setup
	@SubscribeEvent
	public static void onBlockColorEvent(RegisterColorHandlersEvent.Block event) {
		event.register((state, level, pos, index) -> {
					if(level == null) return FoliageColor.getDefaultColor();
					boolean spruce = state.getBlock() == Blocks.SPRUCE_LEAVES;

					int color = BiomeColors.getAverageFoliageColor(level, pos);
					if(state.getBlock() == Blocks.BIRCH_LEAVES) color = FoliageColor.getBirchColor();
					else if(spruce) color = FoliageColor.getEvergreenColor();

					if(!Furnish.CONFIG.whitenSnowyLeaves.get()) return color;
					else if(spruce && !Furnish.CONFIG.whitenSpruceLeaves.get()) return color;

					if(!isLeaveSnowy(level, pos, spruce, false)) return color;

					int red = (color >> 16 & 0xff);
					int green = (color >> 8 & 0xff);
					int blue = (color & 0xff);
					red += (0xff - red);
					green += (0xff - green);
					blue += (0xff - blue);

					return red << 16 | green << 8 | blue;
				}, Blocks.OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.SPRUCE_LEAVES,
				Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES);
	}
}
