package io.github.wouink.furnish.block;

import io.github.wouink.furnish.block.tileentity.FurnitureTileEntity;
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
import org.jetbrains.annotations.Nullable;

public class InventoryFurniture extends SimpleFurniture implements EntityBlock, IFurnitureWithSound {
	private final RegistryObject<SoundEvent> openSound;
	private final RegistryObject<SoundEvent> closeSound;
	public InventoryFurniture(Properties p, final RegistryObject<SoundEvent> openSound, final RegistryObject<SoundEvent> closeSound) {
		super(p);
		this.openSound = openSound;
		this.closeSound = closeSound;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FurnitureTileEntity(pos, state);
	}

//	@Override
//	public ISidedInventory getContainer(BlockState state, IWorld world, BlockPos pos) {
//		TileEntity tileEntity = world.getBlockEntity(pos);
//		if(tileEntity instanceof ISidedInventoryProvider) {
//			return (ISidedInventory) tileEntity;
//		}
//		return null;
//	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player playerEntity, InteractionHand hand, BlockHitResult hitResult) {
		if(world.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof FurnitureTileEntity) {
				playerEntity.openMenu((MenuProvider) tileEntity);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof FurnitureTileEntity) {
				Containers.dropContents(world, pos, (Container) tileEntity);
			}
		}
		super.onRemove(state, world, pos, newState, moving);
	}

	@Override
	public SoundEvent getOpenSound() {
		return openSound.get();
	}

	@Override
	public SoundEvent getCloseSound() {
		return closeSound.get();
	}
}
