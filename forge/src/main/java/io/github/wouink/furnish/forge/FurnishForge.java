package io.github.wouink.furnish.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.client.renderer.SeatRenderer;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Furnish.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurnishForge {

    public FurnishForge() {
        Furnish.LOG.info("Initializing Furnish on Forge.");
        EventBuses.registerModEventBus(Furnish.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
        Furnish.init();
    }

    private void init(final FMLCommonSetupEvent event) {
        // no-op
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
