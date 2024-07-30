package io.github.wouink.furnish.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.client.renderer.SeatRenderer;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(Furnish.MODID)
@EventBusSubscriber
public class FurnishForge {

    public FurnishForge() {
        Furnish.LOG.info("Initializing Furnish on Forge.");

        EventBusesHooks.whenAvailable(Furnish.MODID, iEventBus -> {
            iEventBus.addListener(this::init);
            iEventBus.addListener(this::initClient);
        });

        Furnish.init();

        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        NeoForge.EVENT_BUS.register(FurnishForgeEvents.class);
    }

    private void init(final FMLCommonSetupEvent event) {
        // no op
    }

    private void initClient(final FMLClientSetupEvent event) {
        Furnish.LOG.info("Initialize Furnish client on Forge.");
        Furnish.initClient();
    }

    // Architectury EntityRendererRegistry.register in FurnishClient does not seem to register the renderer
    // which causes the game to crash on Forge when using any kind of chair
    @SubscribeEvent
    public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(FurnishRegistries.Seat_Entity.get(), SeatRenderer::new);
    }
}
