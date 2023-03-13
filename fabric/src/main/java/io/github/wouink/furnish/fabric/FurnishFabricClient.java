package io.github.wouink.furnish.fabric;

import io.github.wouink.furnish.Furnish;
import net.fabricmc.api.ClientModInitializer;

public class FurnishFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Furnish.LOG.info("Initialize Furnish client on Fabric.");
        Furnish.initClient();
    }
}
