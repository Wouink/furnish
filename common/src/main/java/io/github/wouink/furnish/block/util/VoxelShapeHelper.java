package io.github.wouink.furnish.block.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeHelper {

	// with some help from https://github.com/MrCrayfish/MrCrayfishFurnitureMod/blob/1.16.X/src/main/java/com/mrcrayfish/furniture/util/VoxelShapeHelper.java

	public static VoxelShape[] getMergedShapes(VoxelShape[]...  rotatedShapes) {
		VoxelShape[] ret = new VoxelShape[] { Shapes.empty(), Shapes.empty(), Shapes.empty(), Shapes.empty() };
		for(int i = 0; i < rotatedShapes.length; i++) {
			ret[0] = Shapes.or(ret[0], rotatedShapes[i][0]);
			ret[1] = Shapes.or(ret[1], rotatedShapes[i][1]);
			ret[2] = Shapes.or(ret[2], rotatedShapes[i][2]);
			ret[3] = Shapes.or(ret[3], rotatedShapes[i][3]);
		}
		ret[0] = ret[0].optimize();
		ret[1] = ret[1].optimize();
		ret[2] = ret[2].optimize();
		ret[3] = ret[3].optimize();
		return ret;
	}

	public static VoxelShape[] getRotatedShapes(VoxelShape source) {
		VoxelShape north = rotate(source, Direction.NORTH);
		VoxelShape east = rotate(source, Direction.EAST);
		VoxelShape south = rotate(source, Direction.SOUTH);
		VoxelShape west = rotate(source, Direction.WEST);
		return new VoxelShape[] {north, south, west, east};
	}

	public static VoxelShape rotate(VoxelShape source, Direction dir) {
		double[] adjustedValues = adjustValues(dir, source.min(Direction.Axis.X), source.min(Direction.Axis.Z), source.max(Direction.Axis.X), source.max(Direction.Axis.Z));
		return Shapes.box(adjustedValues[0], source.min(Direction.Axis.Y), adjustedValues[1], adjustedValues[2], source.max(Direction.Axis.Y), adjustedValues[3]);
	}

	public static double[] adjustValues(Direction dir, double v1, double v2, double v3, double v4) {
		switch(dir) {
			case WEST:
				double vt1 = v1;
				v1 = 1.0F - v3;
				double vt2 = v2;
				v2 = 1.0F - v4;
				v3 = 1.0F - vt1;
				v4 = 1.0F - vt2;
				break;
			case NORTH:
				double vt3 = v1;
				v1 = v2;
				v2 = 1.0F - v3;
				v3 = v4;
				v4 = 1.0F - vt3;
				break;
			case SOUTH:
				double vt4 = v1;
				v1 = 1.0F - v4;
				double vt5 = v2;
				v2 = vt4;
				double vt6 = v3;
				v3 = 1.0F - vt5;
				v4 = vt6;
				break;
			default:
				break;
		}
		return new double[] {v1, v2, v3, v4};
	}
}
