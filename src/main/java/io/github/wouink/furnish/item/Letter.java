package io.github.wouink.furnish.item;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.blockentity.MailboxBlockEntity;
import io.github.wouink.furnish.network.OpenItemGUIS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.List;

public class Letter extends Item {
    public static final String ANON_PLAYER = "?";

    public Letter(Properties properties) {
        super(properties);
    }

    /* TODO
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        if(itemStack.has(FurnishContents.LETTER_AUTHOR)) {
            String author = itemStack.get(FurnishContents.LETTER_AUTHOR);
            if(author == ANON_PLAYER)
                list.add(Component.translatable("tooltip.furnish.letter.author_anon").withStyle(ChatFormatting.GRAY));
            else
                list.add(Component.translatable("tooltip.furnish.letter.author", author).withStyle(ChatFormatting.GRAY));
        }
        if(itemStack.has(DataComponents.CONTAINER)) {
            Component attached = itemStack.get(DataComponents.CONTAINER).copyOne().getHoverName();
            list.add(Component.translatable("tooltip.furnish.letter.attachment", attached).withStyle(ChatFormatting.GRAY));
        }
    }
     */

    public static ItemStack addAttachment(ItemStack letter, ItemStack item) {
        if(letter.has(DataComponents.CONTAINER)) return item;
        List<ItemStack> contents = List.of(item);
        letter.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(contents));
        return ItemStack.EMPTY;
    }

    public static ItemStack removeAttachment(ItemStack letter) {
        if(letter.has(DataComponents.CONTAINER)) {
            ItemStack attachment = letter.get(DataComponents.CONTAINER).copyOne();
            letter.remove(DataComponents.CONTAINER);
            return attachment;
        }
        return ItemStack.EMPTY;
    }

    public static void sign(ItemStack letter, String author) {
        if(!letter.has(FurnishContents.LETTER_AUTHOR))
            letter.set(FurnishContents.LETTER_AUTHOR, author);
    }

    public static boolean canEdit(ItemStack letter) {
        return !letter.has(FurnishContents.LETTER_AUTHOR);
    }

    private void openGui(ItemStack letter, Player player, InteractionHand hand) {
        if(player.level().isClientSide()) return;
        player.level().playSound(null, player.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS);
        int slot = hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : Inventory.SLOT_OFFHAND;
        OpenItemGUIS2C request = new OpenItemGUIS2C(slot, letter);
        ServerPlayNetworking.send((ServerPlayer) player, request);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack letter = player.getItemInHand(interactionHand);
        // crouching = change attachment
        if(player.isCrouching()) {
            if(level.isClientSide()) return InteractionResult.SUCCESS;
            if(interactionHand == InteractionHand.OFF_HAND) return InteractionResult.FAIL;

            // let's first try to remove any attachment
            ItemStack result = removeAttachment(letter);

            // if there was an attachment, give it back to the player, display a notification and exit
            if(!result.isEmpty()) {
                if(!player.addItem(result))
                    Containers.dropContents(level, player.blockPosition(), NonNullList.of(result));
                player.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_removed"), true);
                MailboxBlockEntity.playSoundToClient((ServerPlayer) player, FurnishContents.REMOVE_ATTACHMENT, SoundSource.MASTER, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }

            // there was no attachment, so let's try to attach the item in the offhand if any
            ItemStack inOffhand = player.getItemInHand(InteractionHand.OFF_HAND);
            if(inOffhand.isEmpty()) return InteractionResult.FAIL;

            result = addAttachment(letter, inOffhand);
            player.setItemInHand(InteractionHand.OFF_HAND, result);

            // success, let's display a notification and exit
            if(result.isEmpty()) {
                Component attachment = inOffhand.getHoverName();
                player.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_added", attachment), true);
                MailboxBlockEntity.playSoundToClient((ServerPlayer) player, FurnishContents.ADD_ATTACHMENT, SoundSource.MASTER, 1.0f, 1.0f);
                return InteractionResult.SUCCESS;
            }

            // we should never reach that
            return InteractionResult.FAIL;
        }

        // not crouching = open letter
        if(!level.isClientSide()) openGui(letter, player, interactionHand);
        return InteractionResult.SUCCESS;
    }
}
