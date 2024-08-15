package io.github.wouink.furnish.neoforge;

import io.github.wouink.furnish.client.renderer.SeatRenderer;
import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
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

    // Architectury EntityRendererRegistry.register in FurnishClient does not seem to register the renderer
    // which causes the game to crash on Forge when using any kind of chair
    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FurnishRegistries.Seat_Entity.get(), SeatRenderer::new);
    }
}
