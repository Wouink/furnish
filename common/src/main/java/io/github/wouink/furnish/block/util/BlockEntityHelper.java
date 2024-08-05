package io.github.wouink.furnish.block.util;

import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityHelper {

	public static void playSoundToPlayer(ServerPlayer player, SoundEvent sound, SoundSource source, float volume, float pitch) {
		ClientboundSoundPacket pkt = new ClientboundSoundPacket((Holder<SoundEvent>) sound, source, player.getX(), player.getY(), player.getZ(), volume, pitch, player.level().getRandom().nextLong());
		player.connection.send(pkt);
	}

	public static void broadcastUpdate(BlockEntity blockEntity, boolean updateRedstone) {
		blockEntity.setChanged();
		blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_ALL);
		if(updateRedstone) blockEntity.getLevel().updateNeighbourForOutputSignal(blockEntity.getBlockPos(), blockEntity.getBlockState().getBlock());
	}
}
