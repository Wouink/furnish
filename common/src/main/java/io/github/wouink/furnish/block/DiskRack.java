package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.DiskRackTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;



public class DiskRack extends HorizontalDirectionalBlock implements EntityBlock {
	private static final VoxelShape RACK_X = Block.box(0, 0, 4, 16, 2, 12);
	private static final VoxelShape RACK_Z = Block.box(4, 0, 0, 12, 2, 16);

	public DiskRack(Properties p) {
		super(p.noOcclusion());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACING);
	}

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return state.getValue(FACING).getAxis() == Direction.Axis.X ? RACK_X : RACK_Z;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DiskRackTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof DiskRackTileEntity) {
				playerEntity.openMenu((MenuProvider) tileEntity);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof Container) {
				Containers.dropContents(world, pos, (Container) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}
}
