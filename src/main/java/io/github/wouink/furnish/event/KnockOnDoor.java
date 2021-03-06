package io.github.wouink.furnish.event;

import io.github.wouink.furnish.setup.FurnishData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KnockOnDoor {

	// sound plays twice currently.
	// LeftClickBlock is called twice: when clicking and when releasing click, with no way to know when is when...

	@SubscribeEvent
	public static void onDoorHit(PlayerInteractEvent.LeftClickBlock event) {
		if(event.getWorld().isClientSide()) return;
		if(event.getPlayer().isCreative()) return;
		if(event.getPlayer().getItemInHand(event.getHand()).isEmpty()) {
			BlockState hitBlock = event.getWorld().getBlockState(event.getPos());
			if(hitBlock.getBlock() instanceof DoorBlock) {
				if(hitBlock.getMaterial() == Material.METAL) {
					event.getWorld().playSound(null, event.getPos(), FurnishData.Sounds.Iron_Door_Knock.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
				} else event.getWorld().playSound(null, event.getPos(), FurnishData.Sounds.Wooden_Door_Knock.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
			}
		}
	}
}
