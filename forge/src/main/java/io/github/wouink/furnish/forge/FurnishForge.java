package io.github.wouink.furnish.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.wouink.furnish.Furnish;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Furnish.MODID)
public class FurnishForge {

    public FurnishForge() {
        Furnish.LOG.info("Initializing Furnish on Forge.");
        EventBuses.registerModEventBus(Furnish.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::initClient);
    }

    private void init(final FMLCommonSetupEvent event) {
        Furnish.LOG.info("Furnish FML Common Setup event.");
        Furnish.init();
    }

    private void initClient(final FMLClientSetupEvent event) {
        Furnish.LOG.info("Initialize Furnish client on Forge.");
        Furnish.initClient();
    }
}
