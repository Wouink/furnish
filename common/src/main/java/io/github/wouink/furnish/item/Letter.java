package io.github.wouink.furnish.item;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.setup.FurnishClient;
import io.github.wouink.furnish.setup.FurnishRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
	public void appendHoverText(ItemStack letter, Level world, List<Component> tooltip, TooltipFlag tooltipFlag) {
		if(letter.hasTag()) {
			CompoundTag letterTag = letter.getTag();
			if(letterTag.contains("Author")) {
				tooltip.add(Component.translatable("tooltip.furnish.letter.author", letterTag.getString("Author")).withStyle(ChatFormatting.GRAY));
			}
			if(letterTag.contains("Attachment")) {
				ItemStack attachment = ItemStack.of(letterTag.getCompound("Attachment"));
				tooltip.add(Component.translatable("tooltip.furnish.letter.attachment", attachment.getItem().getDescription()).withStyle(ChatFormatting.GRAY));
			}
		}
	}

	public static ItemStack addAttachment(ItemStack letter, ItemStack attachment) {
		CompoundTag letterTag = letter.getOrCreateTag();
		if(!letterTag.contains("Attachment")) {
			CompoundTag attachmentTag = new CompoundTag();
			letterTag.put("Attachment", attachment.save(attachmentTag));
			letter.setTag(letterTag);
			return ItemStack.EMPTY;
		}
		return attachment;
	}

	public static ItemStack removeAttachment(ItemStack letter) {
		CompoundTag letterTag = letter.getOrCreateTag();
		if(letterTag.contains("Attachment")) {
			ItemStack attachment = ItemStack.of(letterTag.getCompound("Attachment"));
			letterTag.remove("Attachment");
			letter.setTag(letterTag);
			return attachment;
		}
		return ItemStack.EMPTY;
	}

	public static void signLetter(ItemStack letter, String author) {
		CompoundTag letterTag = letter.getOrCreateTag();
		if(!letterTag.contains("Author")) letterTag.putString("Author", author);
		letter.setTag(letterTag);
	}

	public static boolean canEditLetter(ItemStack letter) {
		CompoundTag letterTag = letter.getOrCreateTag();
		if(letterTag.contains("Author")) return false;
		return true;
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
