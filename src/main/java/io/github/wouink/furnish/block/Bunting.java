package io.github.wouink.furnish.block;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class Bunting extends Block {
	public static final BooleanProperty Z_AXIS = BooleanProperty.create("z_axis");
	public static final VoxelShape BUNTING_X = Block.box(0, 10, 7, 16, 16, 9);
	public static final VoxelShape BUNTING_Z = Block.box(7, 10, 0, 9, 16, 16);

	public Bunting(Properties p, String registryName) {
		super(p.noCollission().noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(Z_AXIS, false));
		FurnishManager.ModBlocks.register(registryName, this);
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(Z_AXIS);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		if(!ctx.getPlayer().isCrouching()) {
			BlockState buntingCheck = ctx.getLevel().getBlockState(ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite()));
			if (buntingCheck.getBlock() instanceof Bunting)
				return this.defaultBlockState().setValue(Z_AXIS, buntingCheck.getValue(Z_AXIS));
		}
		return this.defaultBlockState().setValue(Z_AXIS, ctx.getHorizontalDirection().getAxis() == Direction.Axis.Z);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return state.getValue(Z_AXIS).booleanValue() ? BUNTING_Z : BUNTING_X;
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
