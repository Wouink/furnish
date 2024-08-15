package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.block.util.VoxelShapeHelper;
import io.github.wouink.furnish.entity.SeatEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Chair extends SimpleFurniture {
	public static final VoxelShape[] BASE_SHAPES = VoxelShapeHelper.getRotatedShapes(Block.box(4, 0, 3, 14, 9, 13));
	public static final VoxelShape[] CHAIR_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 17, 13));
	public static final VoxelShape[] CHAIR_TALL_SEAT = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 22, 13));
	public static final VoxelShape[] CHAIR_SEAT_THRONE = VoxelShapeHelper.getRotatedShapes(Block.box(3, 9, 3, 6, 30, 13));

	private final VoxelShape[] myShapes;
	public Chair(Properties p, VoxelShape[] shapes) {
		super(p.noOcclusion());
		myShapes = shapes;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
		Direction dir = state.getValue(FACING);
		return myShapes[dir.ordinal() - 2];
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		return SeatEntity.create(level, blockPos, 0.5, player);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}
}
