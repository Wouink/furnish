package io.github.wouink.furnish.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class AddArmsToArmorStand {

    public static InteractionResult onInteractWithArmorStand(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult entityHitResult) {
        if(level.isClientSide()) return InteractionResult.PASS;
        if(player.isSpectator()) return InteractionResult.PASS;
        if(!(entity instanceof ArmorStand armorStand)) return InteractionResult.PASS;
        if(armorStand.isShowArms()) return InteractionResult.PASS; // already has arms
        ItemStack inHand = player.getItemInHand(hand);
        if(!inHand.is(Items.STICK)) return InteractionResult.PASS;
        armorStand.setShowArms(true);
        level.playSound(null, armorStand.blockPosition(), SoundEvents.WOOD_PLACE, SoundSource.PLAYERS);
        player.swing(hand);
        if(!player.isCreative()) {
            inHand.shrink(1);
            player.setItemInHand(hand, inHand);
        }
        return InteractionResult.SUCCESS;
    }
}
