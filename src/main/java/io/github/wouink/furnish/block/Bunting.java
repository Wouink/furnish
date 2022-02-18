package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Bunting extends Block {
	public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
	public static final VoxelShape BUNTING_X = Block.box(0, 7, 7, 16, 12, 9);
	public static final VoxelShape BUNTING_Z = Block.box(7, 7, 0, 9, 12, 16);

	public Bunting(Properties p) {
		super(p.noCollission().noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(Z_AXIS, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(Z_AXIS);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		if(!ctx.getPlayer().isShiftKeyDown()) {
			BlockState buntingCheck = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite()));
			if (buntingCheck.getBlock() instanceof Bunting)
				return this.defaultBlockState().setValue(Z_AXIS, buntingCheck.getValue(Z_AXIS));
		}
		return this.defaultBlockState().setValue(Z_AXIS, ctx.getHorizontalDirection().getAxis() == Direction.Axis.Z);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return state.getValue(Z_AXIS).booleanValue() ? BUNTING_Z : BUNTING_X;
	}
}
