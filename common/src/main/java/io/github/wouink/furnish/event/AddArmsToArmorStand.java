package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class AddArmsToArmorStand {

	public static EventResult rightClickArmorStand(Player player, Entity entity, InteractionHand hand) {
		Furnish.debug("Right click entity " + entity);
		Level level = player.getLevel();
		if(level.isClientSide()) return EventResult.pass();
		if(entity instanceof ArmorStand armorStand) {
			if(!armorStand.isShowArms()) {
				ItemStack heldItem = player.getItemInHand(hand);
				if(heldItem.getItem() == Items.STICK) {
					SynchedEntityData data = armorStand.getEntityData();
					data.set(ArmorStand.DATA_CLIENT_FLAGS, setBit(data.get(ArmorStand.DATA_CLIENT_FLAGS), 4, true));

					if(!player.isCreative()) {
						heldItem.shrink(1);
						player.setItemInHand(hand, heldItem);
					}

					level.playSound(null, armorStand.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);
					player.swing(hand);

					return EventResult.interruptFalse();
				}
			}
		}
		return EventResult.pass();
	}

	private static byte setBit(byte b, int n, boolean v) {
		return v ? (byte)(b | n) : (byte)(b & ~n);
	}
}
