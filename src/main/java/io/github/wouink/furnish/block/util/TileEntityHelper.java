package io.github.wouink.furnish.block.util;

import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TileEntityHelper {

	public static void playSoundToPlayer(ServerPlayer player, SoundEvent sound, SoundSource source, float volume, float pitch) {
		player.connection.send(new ClientboundCustomSoundPacket(sound.getLocation(), source, player.position(), volume, pitch, player.getLevel().getRandom().nextLong()));
	}

	public static void broadcastUpdate(BlockEntity blockEntity, boolean updateRedstone) {
		blockEntity.setChanged();
		blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_ALL);
		if(updateRedstone) blockEntity.getLevel().updateNeighbourForOutputSignal(blockEntity.getBlockPos(), blockEntity.getBlockState().getBlock());
	}
}
