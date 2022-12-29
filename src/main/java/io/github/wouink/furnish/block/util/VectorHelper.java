package io.github.wouink.furnish.block.util;

import com.mojang.math.Vector3d;
import net.minecraft.core.Direction;

public class VectorHelper {
	public static Vector3d rotateVectorForRenderer(Vector3d source, Direction dir) {
		switch(dir) {
			case SOUTH:
				return new Vector3d(1 - source.z, source.y, source.x);
			case WEST:
				return new Vector3d(-source.x, source.y, 1 - source.z);
			case NORTH:
				return new Vector3d(source.z, source.y, -source.x);
			default:
				return source;
		}
	}
}
