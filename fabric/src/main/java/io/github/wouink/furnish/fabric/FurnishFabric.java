package io.github.wouink.furnish.fabric;

import io.github.wouink.furnish.Furnish;
import net.fabricmc.api.ModInitializer;

public class FurnishFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Furnish.LOG.info("Initializing Furnish on Fabric.");
        Furnish.init();
    }
}
