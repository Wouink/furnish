package io.github.wouink.furnish.neoforge;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.client.gui.FurnitureWorkbenchScreen;
import io.github.wouink.furnish.client.renderer.SeatRenderer;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@Mod(Furnish.MODID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class FurnishForge {

    public FurnishForge(IEventBus eventBus) {
        Furnish.LOG.info("Initializing Furnish on Forge.");

        eventBus.addListener(this::init);
        eventBus.addListener(this::initClient);

        Furnish.init();

        // Architectury InteractionEvent.INTERACT_ENTITY does not seem to work with Armor Stands and Paintings...
        //NeoForge.EVENT_BUS.register(FurnishForgeEvents.class);
    }

    private void init(final FMLCommonSetupEvent event) {
        // no op
    }

    private void initClient(final FMLClientSetupEvent event) {
        Furnish.LOG.info("Initialize Furnish client on Forge.");
        Furnish.initClient();
    }

    @SubscribeEvent
    private static void registerScreens(RegisterMenuScreensEvent event) {
        // Registering through Architectury does not work anymore in 1.21
        Furnish.LOG.info("RegisterMenuScreensEvent");
        event.register(FurnishRegistries.Furniture_Workbench_Container.get(), FurnitureWorkbenchScreen::new);
    }

    @SubscribeEvent
    private static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        // Registering through Architectury does not work anymore in 1.21
        event.registerEntityRenderer(FurnishRegistries.Seat_Entity.get(), SeatRenderer::new);
    }
}
