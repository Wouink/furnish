package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.util.TileEntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class StackHoldingBlockEntity extends BlockEntity {
	private ItemStack holding;

	public StackHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		holding = ItemStack.EMPTY;
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		holding = ItemStack.of(nbt.getCompound("Held"));
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put("Held", holding.save(new CompoundTag()));
	}

	public ItemStack getHeldItem() {
		return holding;
	}

	public ItemStack swap(ItemStack newStack) {
		ItemStack ret = holding;
		holding = newStack;
		TileEntityHelper.broadcastUpdate(this, true);
		return ret;
	}

	// communication between client/server for rendering purposes

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	/*
	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		this.load(pkt.getTag());
	}
	 */

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	/*
	@Override
	public void handleUpdateTag(CompoundTag tag) {
		load(tag);
	}
	 */
}
