package io.github.wouink.furnish.block.tileentity;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.block.FlowerPot;
import io.github.wouink.furnish.block.util.TileEntityHelper;
import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FlowerPotTileEntity extends RandomizableContainerBlockEntity {
	public static final TagKey VALID_PLANTS = TagKey.create(Registries.ITEM, new ResourceLocation(Furnish.MODID, "plants"));
	protected NonNullList<ItemStack> inventory;

	public FlowerPotTileEntity(BlockPos p_155630_, BlockState p_155631_) {
		super(FurnishData.TileEntities.TE_Flower_Pot.get(), p_155630_, p_155631_);
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
	protected Component getDefaultName() {
		return null;
	}

	@Override
	protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
		// this BlockEntity is not intended to be opened in an inventory
		return null;
	}

	@Override
	public int getContainerSize() {
		return ((FlowerPot)this.getBlockState().getBlock()).getPlants();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if(!this.trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, inventory);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if(!this.tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, inventory);
	}

	public ItemStack setPlant(int slot, ItemStack stack) {
		ItemStack ret = ItemStack.EMPTY;
		if(stack.is(VALID_PLANTS) || stack.isEmpty()) {
			ret = inventory.get(slot);
			inventory.set(slot, stack);
		}
		setItems(inventory);
		TileEntityHelper.broadcastUpdate(this, true);
		return ret;
	}

	public final NonNullList<ItemStack> getItemsForRender() {
		return getItems();
	}

	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = this.saveWithoutMetadata();
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
}
