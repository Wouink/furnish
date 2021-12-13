package io.github.wouink.furnish.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.network.ItemStackUpdateMessage;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.fonts.TextInputUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class LetterScreen extends Screen {
	private static final ITextComponent SCREEN_NAME = new TranslationTextComponent("item.furnish.letter");
	private static final TranslationTextComponent SIGN_LETTER = new TranslationTextComponent("book.signButton");
	private static final ResourceLocation LETTER_BACKGROUND = new ResourceLocation(Furnish.MODID, "textures/gui/letter.png");

	// Max length is 16 lines of 18 characters
	private static final int LETTER_MAX_LENGTH = 288;

	private Hand hand;
	private ItemStack letter;
	private PlayerEntity playerEntity;
	private boolean editable = false;
	private String letterText;
	private TextInputUtil letterEdit = null;
	private int frameTick = 0;

	public LetterScreen(ItemStack letter, PlayerEntity playerEntity, Hand hand) {
		super(SCREEN_NAME);
		this.hand = hand;
		this.letter = letter;
		this.playerEntity = playerEntity;
		CompoundNBT tag = letter.getOrCreateTag();
		letterText = tag.getAllKeys().contains("Text") ? tag.getString("Text") : "";
		editable = Letter.canEditLetter(letter);
		if(editable) {
			letterEdit = new TextInputUtil(this::getText, this::setText, this::getClipboard, this::setClipboard, (s) -> s.length() < LETTER_MAX_LENGTH);
		}
	}

	private void setClipboard(String s) {
		if (this.minecraft != null) {
			TextInputUtil.setClipboardContents(this.minecraft, s);
		}
	}

	private String getClipboard() {
		return this.minecraft != null ? TextInputUtil.getClipboardContents(this.minecraft) : "";
	}

	private String getText() {
		return letterText;
	}

	private void setText(String s) {
		letterText = s;
	}

	private void save() {
		CompoundNBT tag = letter.getOrCreateTag();
		tag.putString("Text", getText());
		letter.setTag(tag);
	}

	private void sendUpdate() {
		int slot = this.hand == Hand.MAIN_HAND ? this.playerEntity.inventory.selected : 40;
		Furnish.networkChannel.sendToServer(new ItemStackUpdateMessage(slot, letter));
	}

	@Override
	protected void init() {
		super.init();
		if(editable) {
			this.addButton(new Button(this.width / 2 - 102, 196, 100, 20, SIGN_LETTER, (var) -> {
				save();
				Letter.signLetter(letter, playerEntity.getGameProfile().getName());
				sendUpdate();
				this.minecraft.setScreen(null);
			}));
			this.addButton(new Button(this.width / 2 + 2, 196, 100, 20, DialogTexts.GUI_DONE, (var) -> {
				save();
				sendUpdate();
				this.minecraft.setScreen(null);
			}));
		} else {
			this.addButton(new Button(this.width / 2 - 50, 196, 100, 20, DialogTexts.GUI_DONE, (var) -> {
				this.minecraft.setScreen(null);
			}));
		}
	}

	@Override
	public boolean charTyped(char c, int n) {
		if(super.charTyped(c, n)) {
			return true;
		}
		if(editable && SharedConstants.isAllowedChatCharacter(c)) {
			letterEdit.insertText(Character.toString(c));
			return true;
		}
		return false;
	}

	@Override
	public boolean keyPressed(int key, int m, int n) {
		if(key == 256 && shouldCloseOnEsc()) {
			onClose();
			return true;
		}
		if(editable) {
			if(Screen.isSelectAll(key)) {
				letterEdit.selectAll();
				return true;
			} else if(Screen.isCopy(key)) {
				letterEdit.copy();
				return true;
			} else if(Screen.isCut(key)) {
				letterEdit.cut();
				return true;
			} else if(Screen.isPaste(key)) {
				letterEdit.paste();
				return true;
			} else {
				switch(key) {
					case 257:
					case 335:
						letterEdit.insertText("\n");
						return true;
					case 259:
						letterEdit.removeCharsFromCursor(-1);
						return true;
					default:
						return false;
				}
			}
		}
		return false;
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.minecraft.getTextureManager().bind(LETTER_BACKGROUND);
		int startX = (this.width - 192) / 2;
		this.blit(ms, startX, 2, 0, 0, 192, 192);

		// text and cursor rendering
		if (editable) {
			if(frameTick / 6 % 2 == 0) {
				font.drawWordWrap(new StringTextComponent(letterText).append("_"), startX + 36, 20, 108, 0);
			} else {
				font.drawWordWrap(new StringTextComponent(letterText).append(" "), startX + 36, 20, 108, 0);
			}
		} else {
			font.drawWordWrap(new StringTextComponent(letterText), startX + 36, 20, 108, 0);
		}
	}

	@Override
	public void renderBackground(MatrixStack ms) {
		super.renderBackground(ms);
		RenderSystem.color4f(1.0f, 1.0f, 1.0F, 1.0f);
	}

	@Override
	public void tick() {
		super.tick();
		if(editable) ++frameTick;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public void onClose() {
		super.onClose();
	}
}
