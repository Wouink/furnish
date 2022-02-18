package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.LargeFurnitureTileEntity;
import io.github.wouink.furnish.block.util.IFurnitureWithSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

public class WideInventoryFurniture extends WideFurniture implements EntityBlock, IFurnitureWithSound {
	private final RegistryObject<SoundEvent> openSound;
	private final RegistryObject<SoundEvent> closeSound;
	public WideInventoryFurniture(Properties p, final RegistryObject<SoundEvent> openSound, final RegistryObject<SoundEvent> closeSound) {
		super(p);
		this.openSound = openSound;
		this.closeSound = closeSound;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		BlockEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
		if (tileEntity instanceof Container) {
			Containers.dropContents(world, pos, (Container) tileEntity);
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
			if(tileEntity instanceof LargeFurnitureTileEntity) {
				playerEntity.openMenu((MenuProvider) tileEntity);
			}
			return InteractionResult.CONSUME;
		}
	}

//	@Override
//	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
//		TileEntity tileEntity = state.getValue(RIGHT).booleanValue() ? world.getBlockEntity(pos.relative(state.getValue(FACING).getClockWise())) : world.getBlockEntity(pos);
//		if(tileEntity instanceof ISidedInventoryProvider) {
//			return (ISidedInventory) tileEntity;
//		}
//		return null;
//	}

	@Override
	public SoundEvent getOpenSound() {
		return openSound.get();
	}

	@Override
	public SoundEvent getCloseSound() {
		return closeSound.get();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new LargeFurnitureTileEntity(pos, state);
	}
}
