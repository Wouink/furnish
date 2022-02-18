package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;

import javax.annotation.Nullable;

public class WideFurniture extends HorizontalDirectionalBlock {
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	public WideFurniture(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, RIGHT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos clicked = ctx.getClickedPos();
		if(ctx.getLevel().getBlockState(clicked.relative(ctx.getHorizontalDirection().getCounterClockWise().getOpposite())).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		} else {
			if(ctx.getLevel().isClientSide()) {
				ctx.getPlayer().displayClientMessage(new TranslatableComponent("msg.furnish.furniture_too_big"), true);
			}
			return null;
		}
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(state, world, pos, oldState, moving);
		if(!state.getValue(RIGHT).booleanValue()) {
			world.setBlock(pos.relative(state.getValue(FACING).getCounterClockWise()), state.setValue(RIGHT, true), 3);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getValue(RIGHT).booleanValue()) {
			if(world.getBlockState(pos.relative(state.getValue(FACING).getClockWise())).is(this)) {
				world.removeBlock(pos.relative(state.getValue(FACING).getClockWise()), true);
			}
		} else {
			if(world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise())).is(this)) {
				world.removeBlock(pos.relative(state.getValue(FACING).getCounterClockWise()), false);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
