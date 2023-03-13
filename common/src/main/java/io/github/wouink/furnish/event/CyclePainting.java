package io.github.wouink.furnish.event;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclePainting {

	public static List<PaintingVariant> getSimilarSizeArt(PaintingVariant art) {
		List<PaintingVariant> similar = new ArrayList<>();
		for(PaintingVariant p : Registry.PAINTING_VARIANT.stream().toList()) {
			if(p.getWidth() == art.getWidth() && p.getHeight() == art.getHeight()) {
				similar.add(p);
			}
		}
		return similar;
	}

	@SubscribeEvent
	public static void onPaintingInteract(PlayerInteractEvent.EntityInteract event) {
		Level world = event.getLevel();
		if(world.isClientSide()) return;
		if(!event.getItemStack().getItem().equals(Items.PAINTING)) return;
		if(!(event.getTarget() instanceof Painting)) return;

		Painting target = (Painting) event.getTarget();
		PaintingVariant newArt = null;

		List<PaintingVariant> similarSizeArts = getSimilarSizeArt(target.getVariant().get());
		if(similarSizeArts.isEmpty() || similarSizeArts.size() < 2) {
			event.getEntity().displayClientMessage(Component.translatable("msg.furnish.cycle_no_painting"), true);
			return;
		}

		// for(PaintingType p : similarSizeArts) System.out.println(p.getRegistryName().toString());

		if(event.getEntity().isCrouching()) Collections.reverse(similarSizeArts);
		int index = similarSizeArts.indexOf(target.getVariant().get());
		newArt = similarSizeArts.get((index + 1) % similarSizeArts.size());

		// System.out.println(newArt.getRegistryName().toString());

		// doesn't update
		// target.PaintingVariant = newArt;

		 Painting newPainting = new Painting(world, target.getPos(), target.getMotionDirection(), Holder.direct(newArt));
		 target.remove(Entity.RemovalReason.DISCARDED);
		 world.addFreshEntity(newPainting);
	}
}
