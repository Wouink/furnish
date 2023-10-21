package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SimpleFurniture extends HorizontalDirectionalBlock {
	private boolean supportShape = true;

	public SimpleFurniture(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public SimpleFurniture(Properties p, boolean _supportShape) {
		this(p);
		this.supportShape = _supportShape;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState p_60581_, BlockGetter p_60582_, BlockPos p_60583_) {
		return supportShape ? super.getBlockSupportShape(p_60581_, p_60582_, p_60583_) : Shapes.empty();
	}
}
