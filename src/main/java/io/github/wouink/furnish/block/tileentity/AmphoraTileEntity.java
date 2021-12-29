package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AmphoraTileEntity extends LockableLootTileEntity {
	protected NonNullList<ItemStack> inventory;
	protected AmphoraTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public AmphoraTileEntity() {
		super(FurnishData.TileEntities.TE_Amphora.get());
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
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		ItemStackHelper.saveAllItems(nbt, inventory);
		return nbt;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(nbt, inventory);
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(String.format("block.%s.%s", Furnish.MODID, this.getBlockState().getBlock().getRegistryName().getPath()));
	}

	@Override
	protected Container createMenu(int windowId, PlayerInventory playerInventory) {
		return new ChestContainer(ContainerType.GENERIC_3x3, windowId, playerInventory, this, 1);
	}

	@Override
	public int getContainerSize() {
		return 9;
	}

	@Override
	public void startOpen(PlayerEntity playerEntity) {
		if(!playerEntity.isSpectator()) playSound(FurnishData.Sounds.Amphora_Open.get());
	}

	@Override
	public void stopOpen(PlayerEntity playerEntity) {
		if(!playerEntity.isSpectator()) playSound(FurnishData.Sounds.Amphora_Close.get());
	}

	// copied from net.minecraft.tileentity.BarrelTileEntity
	private void playSound(SoundEvent sound) {
		Vector3i vector3i = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
		double x = (double) this.worldPosition.getX() + 0.5D + (double) vector3i.getX() / 2.0D;
		double y = (double) this.worldPosition.getY() + 0.5D + (double) vector3i.getY() / 2.0D;
		double z = (double) this.worldPosition.getZ() + 0.5D + (double) vector3i.getZ() / 2.0D;
		this.level.playSound(null, x, y, z, sound, SoundCategory.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
	}
}
