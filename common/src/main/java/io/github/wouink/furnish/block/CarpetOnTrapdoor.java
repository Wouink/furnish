package io.github.wouink.furnish.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CarpetOnTrapdoor extends HorizontalDirectionalBlock {
	private static final VoxelShape CLOSED_SHAPE = Block.box(0, 0, 0, 16, 1, 16);
	public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
	private final Block clone;
	public CarpetOnTrapdoor(Properties p, Block _clone) {
		super(p.noOcclusion());
		clone = _clone;
		registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(OPEN, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, OPEN);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState fromState, LevelAccessor world, BlockPos pos, BlockPos fromPos) {
		BlockState below = world.getBlockState(pos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			if(below.getValue(TrapDoorBlock.HALF) == Half.TOP) return state.setValue(OPEN, below.getValue(TrapDoorBlock.OPEN));
		} else if(world.isEmptyBlock(pos.below())) return Blocks.AIR.defaultBlockState();
		return state;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return state.getValue(OPEN).booleanValue() ? Shapes.empty() : CLOSED_SHAPE;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter reader, BlockPos pos, BlockState state) {
		return new ItemStack(clone);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		BlockState below = world.getBlockState(pos.below());
		if(below.getBlock() instanceof TrapDoorBlock) {
			return below.use(world, playerEntity, hand, hitResult.withPosition(pos.below()));
		}
		return InteractionResult.FAIL;
	}
}
