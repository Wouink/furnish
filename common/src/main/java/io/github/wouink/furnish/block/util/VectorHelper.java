package io.github.wouink.furnish.block.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class VectorHelper {
	public static Vec3 rotateVectorForRenderer(Vec3 source, Direction dir) {
		switch(dir) {
			case SOUTH:
				return new Vec3(1 - source.z, source.y, source.x);
			case WEST:
				return new Vec3(-source.x, source.y, 1 - source.z);
			case NORTH:
				return new Vec3(source.z, source.y, -source.x);
			default:
				return source;
		}
	}
}
