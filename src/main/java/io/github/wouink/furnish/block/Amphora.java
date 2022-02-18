package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.AmphoraTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class Amphora extends SimpleFurniture implements EntityBlock {
	public static final VoxelShape AMPHORA_BODY = Block.box(2, 0, 2, 14, 13, 14);
	public static final VoxelShape AMPHORA_TOP = Block.box(4, 13, 4, 12, 16, 12);
	public static final VoxelShape AMPHORA = Shapes.or(AMPHORA_BODY, AMPHORA_TOP).optimize();
	public Amphora(Properties p) {
		super(p);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
		return AMPHORA;
	}

//	@Override
//	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
//		TileEntity tileEntity = world.getBlockEntity(pos);
//		if(tileEntity instanceof ISidedInventoryProvider) {
//			return (ISidedInventory) tileEntity;
//		}
//		return null;
//	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new AmphoraTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof AmphoraTileEntity) {
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
