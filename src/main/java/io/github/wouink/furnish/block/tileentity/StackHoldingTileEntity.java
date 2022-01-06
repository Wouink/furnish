package io.github.wouink.furnish.block.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public abstract class StackHoldingTileEntity extends TileEntity {
	private ItemStack holding;
	private final int packetId;

	public StackHoldingTileEntity(TileEntityType<?> type, int packetId) {
		super(type);
		this.packetId = packetId;
		holding = ItemStack.EMPTY;
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		holding = ItemStack.of(nbt.getCompound("Held"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.put("Held", holding.save(new CompoundNBT()));
		return nbt;
	}

	public ItemStack getHeldItem() {
		return holding;
	}

	public ItemStack swap(ItemStack newStack) {
		ItemStack ret = holding;
		holding = newStack;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
		return ret;
	}

	// communication between client/server for rendering purposes

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.worldPosition, packetId, save(new CompoundNBT()));
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		BlockState state = this.getBlockState();
		load(state, pkt.getTag());
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag) {
		load(state, tag);
	}
}
