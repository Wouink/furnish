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

public class TallFurniture extends HorizontalBlock {
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	public TallFurniture(Properties p, String registryName) {
		super(p);
		setRegistryName(registryName);
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TOP, false));
	}

	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, TOP);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		BlockPos clicked = ctx.getClickedPos();
		if(ctx.getLevel().getBlockState(clicked.above()).canBeReplaced(ctx)) {
			return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
		} else {
			if(ctx.getLevel().isClientSide()) {
				ctx.getPlayer().displayClientMessage(new TranslationTextComponent("furnish.msg.too_big"), true);
			}
			return null;
		}
	}

	@Override
	public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(state, world, pos, oldState, moving);
		if(!state.getValue(TOP).booleanValue()) {
			world.setBlock(pos.above(), state.setValue(TOP, true), 3);
		}
	}

	@Override
	public PushReaction getPistonPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean moving) {
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
