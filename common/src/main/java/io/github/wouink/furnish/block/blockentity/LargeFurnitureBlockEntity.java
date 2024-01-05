package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.util.IFurnitureWithSound;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LargeFurnitureBlockEntity extends RandomizableContainerBlockEntity {
	protected NonNullList<ItemStack> inventory;

	public LargeFurnitureBlockEntity(BlockPos pos, BlockState state) {
		super(FurnishRegistries.Large_Furniture_BlockEntity.get(), pos, state);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> stacks) {
		inventory = stacks;
	}

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if(!this.trySaveLootTable(nbt)) {
			ContainerHelper.saveAllItems(nbt, inventory);
		}
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if(!this.tryLoadLootTable(nbt)) {
			ContainerHelper.loadAllItems(nbt, inventory);
		}
	}

	@Override
	protected Component getDefaultName() {
		return this.getBlockState().getBlock().getName();
	}

	@Override
	protected AbstractContainerMenu createMenu(int syncId, Inventory inv) {
		return new ChestMenu(MenuType.GENERIC_9x6, syncId, inv, this, 6);
	}

	@Override
	public int getContainerSize() {
		return 54;
	}

	@Override
	public void startOpen(Player playerEntity) {
		if(!playerEntity.isSpectator()) playSound(((IFurnitureWithSound) this.getBlockState().getBlock()).getOpenSound());
	}

	@Override
	public void stopOpen(Player playerEntity) {
		if(!playerEntity.isSpectator()) playSound(((IFurnitureWithSound) this.getBlockState().getBlock()).getCloseSound());
	}

	// copied from net.minecraft.tileentity.BarrelTileEntity
	private void playSound(SoundEvent sound) {
		Vec3i vector3i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
		double x = (double) this.worldPosition.getX() + 0.5D + (double) vector3i.getX() / 2.0D;
		double y = (double) this.worldPosition.getY() + 0.5D + (double) vector3i.getY() / 2.0D;
		double z = (double) this.worldPosition.getZ() + 0.5D + (double) vector3i.getZ() / 2.0D;
		this.level.playSound(null, x, y, z, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}
}

