package io.github.wouink.furnish.block.util;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.Vec3;

public class PlacementHelper {

	// code based on Minecraft's DoorBlock class

	public static boolean placeRight(BlockPlaceContext ctx) {
		Direction dir = ctx.getHorizontalDirection();
		int stepX = dir.getStepX();
		int stepZ = dir.getStepZ();
		Vec3 v3d = ctx.getClickLocation();
		double partX = v3d.x - (double) ctx.getClickedPos().getX();
		double partZ = v3d.z - (double) ctx.getClickedPos().getZ();
		return ((stepX >= 0 || !(partZ < 0.5D)) && (stepX <= 0 || !(partZ > 0.5D)) && (stepZ >= 0 || !(partX > 0.5D)) && (stepZ <= 0 || !(partX < 0.5D)));
	}
}
