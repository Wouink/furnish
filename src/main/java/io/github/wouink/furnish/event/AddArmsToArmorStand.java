package io.github.wouink.furnish.event;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class AddArmsToArmorStand {

	@SubscribeEvent
	public static void onStickRightClicked(PlayerInteractEvent.EntityInteractSpecific event) {
		if(event.getWorld().isClientSide()) return;
		if(event.getTarget() instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) event.getTarget();
			if(!armorStand.isShowArms()) {
				ItemStack heldItem = event.getPlayer().getItemInHand(event.getHand());
				if(heldItem.getItem() == Items.STICK) {
					SynchedEntityData data = armorStand.getEntityData();
					// showArms is the 4th bit of ArmorStandEntity.DATA_CLIENT_FLAGS byte
					data.set(ArmorStand.DATA_CLIENT_FLAGS, setBit(data.get(ArmorStand.DATA_CLIENT_FLAGS), 4, true));
					if(!event.getPlayer().isCreative()) {
						heldItem.setCount(heldItem.getCount() - 1);
					}
					// cancel the event to prevent giving the stick to the ArmorStand
					event.setCanceled(true);
				}
			}
		}
	}

	private static byte setBit(byte b, int n, boolean v) {
		return v ? (byte)(b | n) : (byte)(b & ~n);
	}
}
