package io.github.wouink.furnish.block.util;

import net.minecraft.sounds.SoundEvent;

public interface FurnitureWithSound {
	SoundEvent getOpenSound();
	SoundEvent getCloseSound();
}
