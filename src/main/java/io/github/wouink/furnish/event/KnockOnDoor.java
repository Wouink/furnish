package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KnockOnDoor {

	// sound plays twice currently.
	// LeftClickBlock is called twice: when clicking and when releasing click, with no way to know when is when...

	@SubscribeEvent
	public void onDoorHit(PlayerInteractEvent.LeftClickBlock event) {
		if(event.getWorld().isClientSide()) return;
		if(event.getPlayer().isCreative()) return;
		if(event.getPlayer().getItemInHand(event.getHand()).isEmpty()) {
			BlockState hitBlock = event.getWorld().getBlockState(event.getPos());
			if(hitBlock.getBlock() instanceof DoorBlock) {
				if(hitBlock.getMaterial() == Material.METAL) {
					event.getWorld().playSound(null, event.getPos(), FurnishManager.Sounds.Iron_Door_Knock.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
				} else event.getWorld().playSound(null, event.getPos(), FurnishManager.Sounds.Wooden_Door_Knock.get(), SoundCategory.BLOCKS, 1.0f, 1.0f);
			}
		}
	}
}
