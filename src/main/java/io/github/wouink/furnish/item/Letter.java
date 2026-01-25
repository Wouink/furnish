package io.github.wouink.furnish.item;

import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.network.OpenGUIS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.List;

public class Letter extends Item {
    public static final String ANON_PLAYER = "?";
    public static final int GUI_ID = 1;

    public Letter(Properties properties) {
        super(properties);
    }

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
            String attached = itemStack.get(DataComponents.CONTAINER).copyOne().getDescriptionId();
            list.add(Component.translatable("tooltip.furnish.letter.attachment", attached).withStyle(ChatFormatting.GRAY));
        }
    }

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
        OpenGUIS2C request = new OpenGUIS2C(GUI_ID);
        ServerPlayNetworking.send((ServerPlayer) player, request);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack letter = player.getItemInHand(interactionHand);
        // crouching = change attachment
        if(player.isCrouching()) {
            if(level.isClientSide()) return InteractionResultHolder.sidedSuccess(letter, true);
            if(interactionHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(letter);

            // let's first try to remove any attachment
            ItemStack result = removeAttachment(letter);

            // if there was an attachment, give it back to the player, display a notification and exit
            if(!result.isEmpty()) {
                if(!player.addItem(result))
                    Containers.dropContents(level, player.blockPosition(), NonNullList.of(result));
                player.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_removed"), true);
                player.playSound(FurnishContents.REMOVE_ATTACHMENT);
                return InteractionResultHolder.success(letter);
            }

            // there was no attachment, so let's try to attach the item in the offhand if any
            ItemStack inOffhand = player.getItemInHand(InteractionHand.OFF_HAND);
            if(inOffhand.isEmpty()) return InteractionResultHolder.fail(letter);

            result = addAttachment(letter, inOffhand);
            player.setItemInHand(InteractionHand.OFF_HAND, result);

            // success, let's display a notification and exit
            if(result.isEmpty()) {
                String attachment = inOffhand.getDescriptionId();
                player.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_added", attachment), true);
                player.playSound(FurnishContents.ADD_ATTACHMENT);
                return InteractionResultHolder.success(letter);
            }

            // we should never reach that
            return InteractionResultHolder.fail(letter);
        }
        // not crouching = open letter
        if(!level.isClientSide()) openGui(letter, player, interactionHand);
        return InteractionResultHolder.sidedSuccess(letter, level.isClientSide());
    }
}
