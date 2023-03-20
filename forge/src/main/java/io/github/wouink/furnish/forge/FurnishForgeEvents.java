package io.github.wouink.furnish.forge;

import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FurnishForgeEvents {
    @SubscribeEvent
    public static void rightClickArmorStand(PlayerInteractEvent.EntityInteractSpecific event) {
        AddArmsToArmorStand.rightClickArmorStand(event.getEntity(), event.getTarget(), event.getHand());
    }

    @SubscribeEvent
    public static void rightClickPainting(PlayerInteractEvent.EntityInteract event) {
        CyclePainting.onPaintingInteract(event.getEntity(), event.getTarget(), event.getHand());
    }
}
