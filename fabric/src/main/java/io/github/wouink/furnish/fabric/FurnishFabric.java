package io.github.wouink.furnish.fabric;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.fabriclike.FurnishFabricLike;
import net.fabricmc.api.ModInitializer;

public class FurnishFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Furnish.LOG.info("Initializing Furnish on Fabric.");
        Furnish.init();
        FurnishFabricLike.registerFabricSpecificEvents();
    }
}
