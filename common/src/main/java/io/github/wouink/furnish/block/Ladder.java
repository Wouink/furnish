package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;



public class Ladder extends HorizontalDirectionalBlock {
	private static final VoxelShape[] SHAPES = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 3, 16, 16));

	public Ladder(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(FACING).ordinal() - 2];
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		ItemStack stack = player.getItemInHand(hand);
		if(!(stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof Ladder)) return InteractionResult.FAIL;

		// first option = place down
		BlockPos search = pos.below();
		while(world.getBlockState(search).getBlock() instanceof Ladder) search = search.below();
		if(!world.isEmptyBlock(search)) {
			// second option = place up
			search = pos.above();
			while(world.getBlockState(search).getBlock() instanceof Ladder) search = search.above();
		}

		if(world.isEmptyBlock(search)) {
			world.setBlockAndUpdate(search, blockItem.getBlock().defaultBlockState().setValue(FACING, state.getValue(FACING)));
			if(!player.isCreative()) {
				stack.shrink(1);
				player.setItemInHand(hand, stack);
			}
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;
	}
}
