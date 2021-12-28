package io.github.wouink.furnish.block.tileentity;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class LargeFurnitureTileEntity  extends LockableLootTileEntity {
	protected NonNullList<ItemStack> inventory;

	protected LargeFurnitureTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public LargeFurnitureTileEntity() {
		super(FurnishData.TileEntities.TE_Large_Furniture.get());
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
		return new ChestContainer(ContainerType.GENERIC_9x6, windowId, playerInventory, this, 6);
	}

	@Override
	public int getContainerSize() {
		return 54;
	}
}

