package io.github.wouink.furnish.block;

import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.block.blockentity.LargeFurnitureBlockEntity;
import io.github.wouink.furnish.block.util.FurnitureWithSound;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;



public class TallInventoryFurniture extends TallFurniture implements EntityBlock, FurnitureWithSound {
	private RegistrySupplier<SoundEvent> openSound;
	private RegistrySupplier<SoundEvent> closeSound;
	public TallInventoryFurniture(Properties p, final RegistrySupplier<SoundEvent> openSound, final RegistrySupplier<SoundEvent> closeSound) {
		super(p);
		this.openSound = openSound;
		this.closeSound = closeSound;
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		BlockEntity tileEntity = state.getValue(TOP).booleanValue() ? world.getBlockEntity(pos.below()) : world.getBlockEntity(pos);
		if (tileEntity instanceof Container) {
			Containers.dropContents(world, pos, (Container) tileEntity);
		}
		super.onRemove(state, world, pos, newState, moving);
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(TOP).booleanValue() ? null : new LargeFurnitureBlockEntity(pos, state);
	}

	public BlockPos getBlockEntity(BlockPos blockPos, Level level, BlockState blockState) {
		return blockState.getValue(TOP).booleanValue() ? blockPos.below() : blockPos;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity blockEntity = level.getBlockEntity(getBlockEntity(blockPos, level, blockState));
			if(blockEntity instanceof LargeFurnitureBlockEntity furniture) {
				player.openMenu(furniture);
			}
			return InteractionResult.CONSUME;
		}
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
