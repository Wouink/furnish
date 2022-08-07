package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.RecycleBinTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class RecycleBin extends Block implements EntityBlock {
	private static final VoxelShape SHAPE = Block.box(2.5, 0, 2.5, 13.5, 16, 13.5);
	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	private final RegistryObject<SoundEvent> sound;

	public RecycleBin(Properties p, RegistryObject<SoundEvent> sound) {
		super(p.noOcclusion());
		this.sound = sound;
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(TRIGGERED);
	}

	public RegistryObject<SoundEvent> getSound() {
		return sound;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RecycleBinTileEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof RecycleBinTileEntity) {
				if(playerEntity.isCrouching()) ((RecycleBinTileEntity) tileEntity).empty();
				else if(!playerEntity.getItemInHand(hand).isEmpty()) {
					playerEntity.setItemInHand(hand, ((RecycleBinTileEntity) tileEntity).addItem(playerEntity.getItemInHand(hand)));
					if(!playerEntity.getItemInHand(hand).isEmpty()) playerEntity.displayClientMessage(Component.translatable("msg.furnish.recycle_bin_full"), true);
				} else playerEntity.openMenu((MenuProvider) tileEntity);
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

	// copied from DispenserBlock
	@Override
	public void neighborChanged(BlockState p_52700_, Level p_52701_, BlockPos p_52702_, Block p_52703_, BlockPos p_52704_, boolean p_52705_) {
		boolean flag = p_52701_.hasNeighborSignal(p_52702_) || p_52701_.hasNeighborSignal(p_52702_.above());
		boolean flag1 = p_52700_.getValue(TRIGGERED);
		if (flag && !flag1) {
			p_52701_.scheduleTick(p_52702_, this, 4);
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(true)), 4);
		} else if (!flag && flag1) {
			p_52701_.setBlock(p_52702_, p_52700_.setValue(TRIGGERED, Boolean.valueOf(false)), 4);
		}
	}

	@Override
	public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
		BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
		if(blockEntity instanceof RecycleBinTileEntity) {
			((RecycleBinTileEntity) blockEntity).empty();
		}
	}

	public boolean hasAnalogOutputSignal(BlockState p_52682_) {
		return true;
	}

	public int getAnalogOutputSignal(BlockState p_52689_, Level p_52690_, BlockPos p_52691_) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_52690_.getBlockEntity(p_52691_));
	}
}
