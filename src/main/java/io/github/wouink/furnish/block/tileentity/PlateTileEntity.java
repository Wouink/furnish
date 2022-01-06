package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishData;

public class PlateTileEntity extends StackHoldingTileEntity {
	public PlateTileEntity() {
		super(FurnishData.TileEntities.TE_Plate.get(), 1235);
	}
}
