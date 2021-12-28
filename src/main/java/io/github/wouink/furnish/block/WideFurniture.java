package io.github.wouink.furnish.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WideFurniture extends HorizontalBlock {
	public static final BooleanProperty RIGHT = BooleanProperty.create("right");
	public WideFurniture(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(RIGHT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, RIGHT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockPos clicked = ctx.getClickedPos();
		if(ctx.getLevel().getBlockState(clicked.relative(ctx.getHorizontalDirection().getCounterClockWise().getOpposite())).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		} else {
			if(ctx.getLevel().isClientSide()) {
				ctx.getPlayer().displayClientMessage(new TranslationTextComponent("msg.furnish.furniture_too_big"), true);
			}
			return null;
		}
	}

	@Override
	public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
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
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getValue(RIGHT).booleanValue()) {
			if(world.getBlockState(pos.relative(state.getValue(FACING).getClockWise())).getBlock().is(this)) {
				world.removeBlock(pos.relative(state.getValue(FACING).getClockWise()), true);
			}
		} else {
			if(world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise())).getBlock().is(this)) {
				world.removeBlock(pos.relative(state.getValue(FACING).getCounterClockWise()), false);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
