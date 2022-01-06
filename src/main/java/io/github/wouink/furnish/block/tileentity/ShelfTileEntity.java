package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.setup.FurnishData;

public class ShelfTileEntity extends StackHoldingTileEntity {
	public ShelfTileEntity() {
		super(FurnishData.TileEntities.TE_Shelf.get(), 1236);
	}
}
