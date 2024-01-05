package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.blockentity.ShowcaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;



public class Showcase extends HorizontalDirectionalBlock implements EntityBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public Showcase(Properties p) {
		super(p.noOcclusion());
		registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING, POWERED);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ShowcaseBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		InteractionResult resultType = InteractionResult.FAIL;
		if(!world.isClientSide()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if (tileEntity instanceof ShowcaseBlockEntity) {
				playerEntity.setItemInHand(hand, ((ShowcaseBlockEntity) tileEntity).swap(playerEntity.getItemInHand(hand)));
				resultType = InteractionResult.SUCCESS;
			}
		}
		return resultType == InteractionResult.SUCCESS ? InteractionResult.sidedSuccess(world.isClientSide()) : resultType;
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
		BlockEntity tileEntity = world.getBlockEntity(pos);
		if(tileEntity instanceof ShowcaseBlockEntity) {
			ItemStack stack = ((ShowcaseBlockEntity) tileEntity).getHeldItem().copy();
			if(!stack.isEmpty()) {
				stack.setCount(1);
				return stack;
			}
		}
		return super.getCloneItemStack(world, pos, state);
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(!state.is(newState.getBlock())) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof ShowcaseBlockEntity) {
				ItemStack stack = ((ShowcaseBlockEntity) tileEntity).getHeldItem();
				if(!stack.isEmpty()) {
					world.addFreshEntity(new ItemEntity(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stack));
				}
			}
			super.onRemove(state, world, pos, newState, moving);
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighbor, BlockPos neighborPos, boolean moving) {
		boolean flag = world.hasNeighborSignal(pos);
		if (flag != state.getValue(POWERED)) {
			world.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), Block.UPDATE_ALL);
		}
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return Shapes.empty();
	}
}
