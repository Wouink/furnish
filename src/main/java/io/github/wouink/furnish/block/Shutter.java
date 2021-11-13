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
import net.minecraft.world.World;

public class Shutter extends HorizontalBlock {
	public static final IntegerProperty STATE = IntegerProperty.create("state", 0, 2);
	public Shutter(Properties p, String registryName) {
		super(p.noOcclusion());
		setRegistryName(registryName);
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(STATE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, STATE);
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
		world.setBlock(pos, state.cycle(STATE), 3);
		return ActionResultType.SUCCESS;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos pos2, boolean moving) {
		super.neighborChanged(state, world, pos, block, pos2, moving);
		if(world.hasNeighborSignal(pos)) {
			world.setBlock(pos, state.setValue(STATE, 0), 3);
		} else {
			world.setBlock(pos, state.setValue(STATE, 2), 3);
		}
	}
}
