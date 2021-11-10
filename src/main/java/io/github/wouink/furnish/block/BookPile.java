package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BookPile extends HorizontalBlock {
	private static final IntegerProperty PLACEMENT = IntegerProperty.create("placement", 0, 2);
	private static final VoxelShape PILE_SHAPE = Block.box(0.0d, 0.0d, 0.0d, 16.0d, 1.0d, 16.0d);
	public BookPile(Properties p, String registryName) {
		super(p.noOcclusion());
		setRegistryName(registryName);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PLACEMENT, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(PLACEMENT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext ctx) {
		return PILE_SHAPE;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		world.setBlockAndUpdate(pos, state.cycle(PLACEMENT));
		return ActionResultType.SUCCESS;
	}
}
