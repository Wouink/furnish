package io.github.wouink.furnish.event;

import dev.architectury.event.EventResult;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

		PaintingVariant newArt = null;
		List<PaintingVariant> similarSizeArts = getSimilarSizeArt(painting.getVariant().value());
		if(similarSizeArts.isEmpty() || similarSizeArts.size() < 2) {
			player.displayClientMessage(Component.translatable("msg.furnish.cycle_no_painting"), true);
			return EventResult.interruptFalse();
		}

		if(player.isShiftKeyDown()) Collections.reverse(similarSizeArts);
		int index = similarSizeArts.indexOf(painting.getVariant().value());
		newArt = similarSizeArts.get((index + 1) % similarSizeArts.size());

		Painting newPainting = new Painting(level, painting.getPos(), painting.getMotionDirection(), Holder.direct(newArt));
		painting.remove(Entity.RemovalReason.DISCARDED);
		level.addFreshEntity(newPainting);

		level.playSound(null, newPainting.blockPosition(), SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS, 1.0f, 1.0f);
		player.swing(hand);

		return EventResult.interruptTrue();
	}

	public static List<PaintingVariant> getSimilarSizeArt(PaintingVariant art) {
		List<PaintingVariant> similar = new ArrayList<>();
		for(PaintingVariant p : BuiltInRegistries.PAINTING_VARIANT.stream().toList()) {
			if(p.getWidth() == art.getWidth() && p.getHeight() == art.getHeight()) {
				similar.add(p);
			}
		}
		return similar;
	}
}
