package io.github.wouink.furnish.event;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclePainting {

	public static List<Motive> getSimilarSizeArt(Motive art) {
		List<Motive> similar = new ArrayList<>();
		for(Motive p : ForgeRegistries.PAINTING_TYPES.getValues()) {
			if(p.getWidth() == art.getWidth() && p.getHeight() == art.getHeight()) {
				similar.add(p);
			}
		}
		return similar;
	}

	@SubscribeEvent
	public static void onPaintingInteract(PlayerInteractEvent.EntityInteract event) {
		Level world = event.getWorld();
		if(world.isClientSide()) return;
		if(!event.getItemStack().getItem().equals(Items.PAINTING)) return;
		if(!(event.getTarget() instanceof Painting)) return;

		Painting target = (Painting) event.getTarget();
		Motive newArt = null;

		List<Motive> similarSizeArts = getSimilarSizeArt(target.motive);
		if(similarSizeArts.isEmpty() || similarSizeArts.size() < 2) {
			event.getPlayer().displayClientMessage(new TranslatableComponent("msg.furnish.cycle_no_painting"), true);
			return;
		}

		// for(PaintingType p : similarSizeArts) System.out.println(p.getRegistryName().toString());

		if(event.getPlayer().isCrouching()) Collections.reverse(similarSizeArts);
		int index = similarSizeArts.indexOf(target.motive);
		newArt = similarSizeArts.get((index + 1) % similarSizeArts.size());

		// System.out.println(newArt.getRegistryName().toString());

		// doesn't update
		// target.motive = newArt;

		 Painting newPainting = new Painting(world, target.getPos(), target.getMotionDirection());
		 newPainting.motive = newArt;
		 target.remove(Entity.RemovalReason.DISCARDED);
		 world.addFreshEntity(newPainting);
	}
}
