package io.github.wouink.furnish.neoforge;

import io.github.wouink.furnish.Furnish;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Furnish.MODID)
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
}
