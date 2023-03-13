package io.github.wouink.furnish.block.util;

import net.minecraft.sounds.SoundEvent;

public interface IFurnitureWithSound {
	SoundEvent getOpenSound();
	SoundEvent getCloseSound();
}
