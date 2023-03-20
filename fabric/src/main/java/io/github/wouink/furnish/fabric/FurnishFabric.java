package io.github.wouink.furnish.fabric;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.event.AddArmsToArmorStand;
import io.github.wouink.furnish.event.CyclePainting;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;

public class FurnishFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Furnish.LOG.info("Initializing Furnish on Fabric.");
        Furnish.init();

        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(!player.isSpectator()) return CyclePainting.onPaintingInteract(player, entity, hand).asMinecraft();
            else return EventResult.pass().asMinecraft();
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if(!player.isSpectator()) return AddArmsToArmorStand.rightClickArmorStand(player, entity, hand).asMinecraft();
            else return EventResult.pass().asMinecraft();
        });
    }
}
