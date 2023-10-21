package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class TallFurniture extends HorizontalDirectionalBlock {
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public TallFurniture(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TOP, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, TOP);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos clicked = ctx.getClickedPos();
		if(ctx.getLevel().getBlockState(clicked.above()).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		} else {
			if(ctx.getLevel().isClientSide()) {
				ctx.getPlayer().displayClientMessage(Component.translatable("msg.furnish.furniture_too_big"), true);
			}
			return null;
		}
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(state, world, pos, oldState, moving);
		if(!state.getValue(TOP).booleanValue()) {
			world.setBlock(pos.above(), state.setValue(TOP, true), Block.UPDATE_ALL);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getValue(TOP).booleanValue()) {
			if(world.getBlockState(pos.below()).is(this) && !world.getBlockState(pos.below()).getValue(TOP).booleanValue()) {
				world.removeBlock(pos.below(), true);
			}
		} else {
			if(world.getBlockState(pos.above()).is(this) && world.getBlockState(pos.above()).getValue(TOP).booleanValue()) {
				world.removeBlock(pos.above(), false);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
