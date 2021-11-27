package io.github.wouink.furnish.event;

import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclePainting {

	public static List<PaintingType> getSimilarSizeArt(PaintingType art) {
		List<PaintingType> similar = new ArrayList<>();
		for(PaintingType p : ForgeRegistries.PAINTING_TYPES.getValues()) {
			if(p.getWidth() == art.getWidth() && p.getHeight() == art.getHeight()) {
				similar.add(p);
			}
		}
		return similar;
	}

	@SubscribeEvent
	public void onPaintingInteract(PlayerInteractEvent.EntityInteract event) {
		World world = event.getWorld();
		if(world.isClientSide()) return;
		if(!event.getItemStack().getItem().equals(Items.PAINTING)) return;
		if(!(event.getTarget() instanceof PaintingEntity)) return;

		PaintingEntity target = (PaintingEntity) event.getTarget();
		PaintingType newArt = null;

		List<PaintingType> similarSizeArts = getSimilarSizeArt(target.motive);
		if(similarSizeArts.isEmpty() || similarSizeArts.size() < 2) {
			event.getPlayer().displayClientMessage(new TranslationTextComponent("furnish.msg.cycle_no_painting"), true);
			return;
		}

		// for(PaintingType p : similarSizeArts) System.out.println(p.getRegistryName().toString());

		if(event.getPlayer().isCrouching()) Collections.reverse(similarSizeArts);
		int index = similarSizeArts.indexOf(target.motive);
		newArt = similarSizeArts.get((index + 1) % similarSizeArts.size());

		// System.out.println(newArt.getRegistryName().toString());

		// doesn't update
		// target.motive = newArt;

		 PaintingEntity newPainting = new PaintingEntity(world, target.getPos(), target.getMotionDirection());
		 newPainting.motive = newArt;
		 target.remove();
		 world.addFreshEntity(newPainting);
	}
}
