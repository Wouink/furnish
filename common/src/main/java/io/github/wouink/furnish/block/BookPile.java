package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BookPile extends HorizontalDirectionalBlock {
	private static final IntegerProperty PLACEMENT = IntegerProperty.create("placement", 0, 2);
	private static final VoxelShape PILE_SHAPE = Block.box(4, 0, 4, 12, 8, 12);
	private static final VoxelShape LAYING_SHAPE = Block.box(0, 0, 0, 16, 2, 16);
	public BookPile(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PLACEMENT, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
		builder.add(PLACEMENT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return state.getValue(PLACEMENT).intValue() == 2 ? LAYING_SHAPE : PILE_SHAPE;
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		world.setBlockAndUpdate(pos, state.cycle(PLACEMENT));
		world.playSound(playerEntity, pos, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0f, 1.0f);
		return InteractionResult.SUCCESS;
	}
}
