package io.github.wouink.furnish.block;

import net.minecraft.sounds.SoundEvent;

public interface SoundProvider {
    SoundEvent getOpenSound();
    SoundEvent getCloseSound();
}
