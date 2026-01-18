package io.github.wouink.furnish.block.util;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.Vec3;

public class PlacementHelper {

    /**
     * Based on DoorBlock class from Minecraft
     * @param ctx the context
     * @return true if the hinge should be on the right side
     */
    public static boolean shouldPlaceRight(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection();
        int stepX = dir.getStepX();
        int stepZ = dir.getStepZ();
        Vec3 v3d = ctx.getClickLocation();
        double partX = v3d.x - (double) ctx.getClickedPos().getX();
        double partZ = v3d.z - (double) ctx.getClickedPos().getZ();
        return ((stepX >= 0 || !(partZ < 0.5D)) && (stepX <= 0 || !(partZ > 0.5D)) && (stepZ >= 0 || !(partX > 0.5D)) && (stepZ <= 0 || !(partX < 0.5D)));
    }
}
