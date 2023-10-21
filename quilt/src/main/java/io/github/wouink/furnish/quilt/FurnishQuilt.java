package io.github.wouink.furnish.quilt;

import io.github.wouink.furnish.fabriclike.FurnishFabricLike;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class FurnishQuilt implements ModInitializer {
    @Override
    public void onInitialize(ModContainer mod) {
        FurnishFabricLike.init();
    }
}
