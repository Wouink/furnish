package io.github.wouink.furnish.block.util;

import net.minecraft.util.SoundEvent;

public interface IFurnitureWithSound {
	SoundEvent getOpenSound();
	SoundEvent getCloseSound();
}
