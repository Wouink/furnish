package io.github.wouink.furnish.block;

import io.github.wouink.furnish.Furnish;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;

public class TableBlock extends Block {
	public TableBlock(Properties p, String registryName) {
		super(p.noOcclusion());
		setRegistryName(Furnish.MODID, registryName);
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
