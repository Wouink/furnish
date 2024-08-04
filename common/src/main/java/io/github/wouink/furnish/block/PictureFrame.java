package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.setup.FurnishBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PictureFrame extends HorizontalDirectionalBlock {
	public static final MapCodec<PictureFrame> CODEC = simpleCodec(PictureFrame::new);
	public static final IntegerProperty COUNT = FurnishBlocks.CustomProperties.COUNT_3;
	private static final VoxelShape INTERACTION = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public PictureFrame(Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(COUNT, 1));
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(COUNT, FACING);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
		if(state.is(this)) return state.cycle(COUNT);
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return INTERACTION;
	}

	@Override
	public VoxelShape getInteractionShape(BlockState p_60547_, BlockGetter p_60548_, BlockPos p_60549_) {
		return INTERACTION;
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext ctx) {
		return !ctx.isSecondaryUseActive() && ctx.getItemInHand().getItem() == this.asItem() && state.getValue(COUNT) < 3;
	}

	@Override
	public BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
		return super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
	}
}
