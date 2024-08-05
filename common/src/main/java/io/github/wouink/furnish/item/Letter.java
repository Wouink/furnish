package io.github.wouink.furnish.item;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishClient;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Letter extends Item {
	public Letter(Properties p) {
		super(p);
	}

	private void openGui(ItemStack stack, Player playerEntity, InteractionHand hand) {
		if(Platform.getEnvironment() != Env.CLIENT) {
			Furnish.LOG.error("Furnish Letter Item - Attempt to call openGui elsewhere than on client.");
			return;
		}
		playerEntity.level().playSound(null, playerEntity.blockPosition(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0f, 1.0f);
		FurnishClient.openLetterGui(stack, playerEntity, hand);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
		if(itemStack.has(FurnishRegistries.Letter_Author.get())) {
			list.add(Component.translatable("tooltip.furnish.letter.author", itemStack.get(FurnishRegistries.Letter_Author.get())).withStyle(ChatFormatting.GRAY));
		}
		if(itemStack.has(DataComponents.CONTAINER)) {
			ItemStack attachment = itemStack.get(DataComponents.CONTAINER).copyOne();
			list.add(Component.translatable("tooltip.furnish.letter.attachment", attachment.getItem().getDescription()).withStyle(ChatFormatting.GRAY));
		}
	}

	public static ItemStack addAttachment(ItemStack letter, ItemStack attachment) {
		if(letter.has(DataComponents.CONTAINER)) {
			return attachment;
		} else {
			ArrayList<ItemStack> contents = new ArrayList(1);
			contents.add(attachment);
			letter.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(contents));
			return ItemStack.EMPTY;
		}
	}

	public static ItemStack removeAttachment(ItemStack letter) {
		if(letter.has(DataComponents.CONTAINER)) {
			ItemStack attachment = letter.get(DataComponents.CONTAINER).copyOne();
			letter.remove(DataComponents.CONTAINER);
			return attachment;
		}
		return ItemStack.EMPTY;
	}

	public static void signLetter(ItemStack letter, String author) {
		if(!letter.has(FurnishRegistries.Letter_Author.get())) {
			letter.set(FurnishRegistries.Letter_Author.get(), author);
		}
	}

	public static boolean canEditLetter(ItemStack letter) {
		return letter.has(FurnishRegistries.Letter_Author.get());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, InteractionHand hand) {
		ItemStack letter = playerEntity.getItemInHand(hand);
		if(playerEntity.isCrouching()) {
			if(hand != InteractionHand.OFF_HAND) {
				ItemStack result = removeAttachment(letter);
				if(!result.isEmpty()) {
					playerEntity.addItem(result);
					if(world.isClientSide()) {
						playerEntity.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_removed"), true);
						playerEntity.playSound(FurnishRegistries.Detach_From_Letter_Sound.get(), 1.0f, 1.0f);
					}
					return InteractionResultHolder.sidedSuccess(letter, world.isClientSide());
				}
				ItemStack offHandStack = playerEntity.getItemInHand(InteractionHand.OFF_HAND);
				if(!offHandStack.isEmpty()) {
					result = addAttachment(letter, offHandStack);
					playerEntity.setItemInHand(InteractionHand.OFF_HAND, result);
					if(result.isEmpty()) {
						if(world.isClientSide()) {
							playerEntity.displayClientMessage(Component.translatable("msg.furnish.letter.attachment_added", offHandStack.getItem().getDescription()), true);
							playerEntity.playSound(FurnishRegistries.Attach_To_Letter_Sound.get(), 1.0f, 1.0f);
						}
						return InteractionResultHolder.sidedSuccess(letter, world.isClientSide());
					}
				}
				return InteractionResultHolder.fail(letter);
			}
			return InteractionResultHolder.fail(letter);
		} else {
			if(world.isClientSide()) openGui(letter, playerEntity, hand);
			return InteractionResultHolder.sidedSuccess(letter, world.isClientSide());
		}
	}
}
