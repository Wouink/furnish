package io.github.wouink.furnish.fabriclike;

import io.github.wouink.furnish.Furnish;
import net.fabricmc.api.ClientModInitializer;

public class FurnishFabricLikeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Furnish.LOG.info("Initialize Furnish client on Fabric-Like platform.");
        Furnish.initClient();
    }
}
