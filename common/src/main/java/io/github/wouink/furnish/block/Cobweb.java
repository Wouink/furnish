package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

// todo implements IForgeShearable
public class Cobweb extends HorizontalDirectionalBlock {
	public static final EnumProperty HALF = BlockStateProperties.HALF;
	public Cobweb(Properties p) {
		super(p.noOcclusion().noCollission());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, HALF);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		Direction dir = ctx.getClickedFace();
		state = state.setValue(HALF, dir != Direction.DOWN && (dir == Direction.UP || !(ctx.getClickLocation().y - (double)ctx.getClickedPos().getY() > 0.5D)) ? Half.BOTTOM : Half.TOP);
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return Shapes.block();
	}

	@Override
	public void entityInside(BlockState p_58180_, Level p_58181_, BlockPos p_58182_, Entity p_58183_) {
		p_58183_.makeStuckInBlock(p_58180_, new Vec3(0.25D, (double)0.05F, 0.25D));
	}
}
