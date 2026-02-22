package io.github.wouink.furnish.event;

import io.github.wouink.furnish.FurnishContents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclePainting {

    public static InteractionResult onInteractWithPainting(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult entityHitResult) {
        if(player.isSpectator()) return InteractionResult.PASS;
        if(!(entity instanceof Painting painting)) return InteractionResult.PASS;
        ItemStack inHand = player.getItemInHand(hand);
        if(!inHand.getItem().equals(Items.PAINTING) || !inHand.is(FurnishContents.CAN_CYCLE))
            return InteractionResult.PASS;

        if(level.isClientSide()) return InteractionResult.SUCCESS;

        List<Holder<PaintingVariant>> similarSizedArts = getSimilarSizeArt(level, painting.getVariant().value());
        if(similarSizedArts.size() < 2) {
            player.displayClientMessage(Component.translatable("msg.furnish.cycle_no_painting"), true);
            return InteractionResult.PASS;
        }

        if(player.isShiftKeyDown()) Collections.reverse(similarSizedArts);

        int index = similarSizedArts.indexOf(painting.getVariant());
        int newVariantIndex = (index + 1) % similarSizedArts.size();
        Holder<PaintingVariant> newVariant = similarSizedArts.get(newVariantIndex);

        System.out.println("Found " + similarSizedArts.size() + " variants, will set to number " + newVariantIndex);

        painting.setVariant(newVariant);

        level.playSound(null, painting.blockPosition(), SoundEvents.PAINTING_PLACE, SoundSource.BLOCKS);
        player.swing(hand);

        return InteractionResult.SUCCESS;
    }

    private static List<Holder<PaintingVariant>> getSimilarSizeArt(Level level, PaintingVariant reference) {
        List<Holder<PaintingVariant>> similar = new ArrayList<>();
        Iterable<Holder<PaintingVariant>> allVariants = level.getServer().registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE);
        for(Holder<PaintingVariant> variantHolder : allVariants) {
            PaintingVariant art = variantHolder.value();
            if(art.width() == reference.width() && art.height() == reference.height())
                similar.add(variantHolder);
        }
        return similar;
    }
}
