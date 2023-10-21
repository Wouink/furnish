package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PaperLamp extends Block {
	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(2, 2, 2, 14, 14, 14),
			Block.box(6, 0, 6, 10, 16, 10)
	);

	public PaperLamp() {
		super(BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.SCAFFOLDING).noOcclusion().lightLevel((state) -> 15));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}
}
