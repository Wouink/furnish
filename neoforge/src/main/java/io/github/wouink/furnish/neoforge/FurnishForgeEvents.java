package io.github.wouink.furnish.neoforge;

import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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
