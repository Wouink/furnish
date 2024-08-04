package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
	public static final MapCodec<Ladder> CODEC = simpleCodec(Ladder::new);
	private static final VoxelShape[] SHAPES = VoxelShapeHelper.getRotatedShapes(Block.box(0, 0, 0, 3, 16, 16));

	public Ladder(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
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
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return ItemInteractionResult.SUCCESS;
		if(!(itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof Ladder)) return ItemInteractionResult.FAIL;

		// first option = place down
		BlockPos search = blockPos.below();
		while(level.getBlockState(search).getBlock() instanceof Ladder) search = search.below();
		if(!level.isEmptyBlock(search)) {
			// second option = place up
			search = blockPos.above();
			while(level.getBlockState(search).getBlock() instanceof Ladder) search = search.above();
		}

		if(level.isEmptyBlock(search)) {
			level.setBlockAndUpdate(search, blockItem.getBlock().defaultBlockState().setValue(FACING, blockState.getValue(FACING)));
			level.playSound(null, search, this.getSoundType(this.defaultBlockState()).getPlaceSound(), SoundSource.BLOCKS, 1.0f, 1.0f);
			if(!player.isCreative()) {
				itemStack.shrink(1);
				player.setItemInHand(interactionHand, itemStack);
			}
			return ItemInteractionResult.SUCCESS;
		}

		return ItemInteractionResult.FAIL;
	}
}
