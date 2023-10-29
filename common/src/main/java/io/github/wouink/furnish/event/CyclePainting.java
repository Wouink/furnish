package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import io.github.wouink.furnish.Furnish;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclePainting {
	public static EventResult onPaintingInteract(Player player, Entity entity, InteractionHand hand) {
		Level level = player.level();
		if(level.isClientSide()) return EventResult.pass();
		if(!player.getItemInHand(hand).getItem().equals(Items.PAINTING)) return EventResult.pass();
		if(!(entity instanceof Painting painting)) return EventResult.pass();

		List<Holder<PaintingVariant>> similarSizeArts = getSimilarSizeArt(level, painting.getVariant().value());
		if(similarSizeArts.isEmpty() || similarSizeArts.size() < 2) {
			player.displayClientMessage(Component.translatable("msg.furnish.cycle_no_painting"), true);
			return EventResult.interruptFalse();
		}

		if(player.isShiftKeyDown()) Collections.reverse(similarSizeArts);

		int index = similarSizeArts.indexOf(painting.getVariant());
		int newVariantIndex = (index + 1) % similarSizeArts.size();
		Furnish.debug("index = " + index + ", newVariantIndex = " + newVariantIndex);
		Holder<PaintingVariant> newVariant = similarSizeArts.get(newVariantIndex);

		painting.setVariant(newVariant);

		level.playSound(null, painting.blockPosition(), SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		player.swing(hand);

		return EventResult.interruptTrue();
	}

	public static List<Holder<PaintingVariant>> getSimilarSizeArt(Level level, PaintingVariant reference) {
		List<Holder<PaintingVariant>> similar = new ArrayList<>();
		Iterable<Holder<PaintingVariant>> allVariants = level.getServer().registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE);
		for(Holder<PaintingVariant> holder : allVariants) {
			PaintingVariant art = holder.value();
			if(art.getWidth() == reference.getWidth() && art.getHeight() == reference.getHeight()) {
				similar.add(holder);
			}
		}
		Furnish.debug("Found " + similar.size() + " painting variants similar to " + reference.toString());
		return similar;
	}
}
