package io.github.wouink.furnish.block;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.wouink.furnish.block.blockentity.FurnitureBlockEntity;
import io.github.wouink.furnish.block.util.IFurnitureWithSound;
import io.github.wouink.furnish.block.util.InteractionHelper;
import io.github.wouink.furnish.setup.FurnishBlocks;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class InventoryFurniture extends SimpleFurniture implements EntityBlock, IFurnitureWithSound {
	public static final MapCodec CODEC = simpleCodec(InventoryFurniture::new);
	private final RegistrySupplier<SoundEvent> openSound;
	private final RegistrySupplier<SoundEvent> closeSound;
	public InventoryFurniture(Properties p, final RegistrySupplier<SoundEvent> openSound, final RegistrySupplier<SoundEvent> closeSound) {
		super(p);
		this.openSound = openSound;
		this.closeSound = closeSound;
		FurnishBlocks.Furniture_3x9.add(this);
	}

	public InventoryFurniture(Properties properties) {
		super(properties);
		openSound = FurnishRegistries.Cabinet_Open_Sound;
		closeSound = FurnishRegistries.Cabinet_Close_Sound;
	}

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FurnitureBlockEntity(pos, state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
		if(level.isClientSide()) return InteractionResult.SUCCESS;
		else {
			BlockEntity tileEntity = level.getBlockEntity(blockPos);
			if(tileEntity instanceof FurnitureBlockEntity) {
				player.openMenu((MenuProvider) tileEntity);
			}
			return InteractionResult.CONSUME;
		}
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return InteractionHelper.toItem(useWithoutItem(blockState, level, blockPos, player, blockHitResult));
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
		if(state.getBlock() != newState.getBlock()) {
			BlockEntity tileEntity = world.getBlockEntity(pos);
			if(tileEntity instanceof FurnitureBlockEntity) {
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

	@Override
	protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
		return CODEC;
	}
}
