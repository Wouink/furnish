package io.github.wouink.furnish.fabriclike;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class FurnishFabricLike {
    public static void init() {
        Furnish.LOG.info("Initialize Furnish on Fabric-Like platform.");
        Furnish.init();
        FurnishFabricLike.registerFabricSpecificEvents();
    }

    public static void registerFabricSpecificEvents() {
        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(!player.isSpectator()) return CyclePainting.onPaintingInteract(player, entity, hand).asMinecraft();
            else return EventResult.pass().asMinecraft();
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(!player.isSpectator()) return AddArmsToArmorStand.rightClickArmorStand(player, entity, hand).asMinecraft();
            else return EventResult.pass().asMinecraft();
        });
        Furnish.LOG.info("Registered Furnish Fabric specific events.");
    }
}
