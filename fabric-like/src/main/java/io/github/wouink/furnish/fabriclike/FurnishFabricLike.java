package io.github.wouink.furnish.fabriclike;

import io.github.wouink.furnish.Furnish;

public class FurnishFabricLike {
    public static void init() {
        Furnish.LOG.info("Initialize Furnish on Fabric-Like platform.");
        Furnish.init();
    }
}
