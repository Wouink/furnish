package io.github.wouink.furnish.block.blockentity;

import io.github.wouink.furnish.block.util.BlockEntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
	protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.loadAdditional(compoundTag, provider);
		if(compoundTag.contains("Held")) {
			holding = ItemStack.parseOptional(provider, compoundTag.getCompound("Held"));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
		super.saveAdditional(compoundTag, provider);
		if(!holding.isEmpty()) compoundTag.put("Held", holding.save(provider, compoundTag));
	}

	public ItemStack getHeldItem() {
		return holding;
	}

	public ItemStack swap(ItemStack newStack) {
		ItemStack ret = holding;
		holding = newStack;
		BlockEntityHelper.broadcastUpdate(this, true);
		return ret;
	}

	// communication between client/server for rendering purposes

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
		return this.saveWithFullMetadata(provider);
	}
}
