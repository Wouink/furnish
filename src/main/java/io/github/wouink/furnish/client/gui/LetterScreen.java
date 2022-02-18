package io.github.wouink.furnish.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.network.ItemStackUpdateMessage;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class LetterScreen extends Screen {
	private static final Component SCREEN_NAME = new TranslatableComponent("item.furnish.letter");
	private static final Component SIGN_LETTER = new TranslatableComponent("book.signButton");
	private static final ResourceLocation LETTER_BACKGROUND = new ResourceLocation(Furnish.MODID, "textures/gui/letter.png");

	// Max length is 16 lines of 18 characters
	private static final int LETTER_MAX_LENGTH = 288;

	private InteractionHand hand;
	private ItemStack letter;
	private Player playerEntity;
	private boolean editable = false;
	private String letterText;
	private TextFieldHelper letterEdit = null;
	private int frameTick = 0;

	public LetterScreen(ItemStack letter, Player playerEntity, InteractionHand hand) {
		super(SCREEN_NAME);
		this.hand = hand;
		this.letter = letter;
		this.playerEntity = playerEntity;
		CompoundTag tag = letter.getOrCreateTag();
		letterText = tag.getAllKeys().contains("Text") ? tag.getString("Text") : "";
		editable = Letter.canEditLetter(letter);
		if(editable) {
			letterEdit = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, (s) -> s.length() < LETTER_MAX_LENGTH);
		}
	}

	private void setClipboard(String s) {
		if (this.minecraft != null) {
			TextFieldHelper.setClipboardContents(this.minecraft, s);
		}
	}

	private String getClipboard() {
		return this.minecraft != null ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
	}

	private String getText() {
		return letterText;
	}

	private void setText(String s) {
		letterText = s;
	}

	private void save() {
		CompoundTag tag = letter.getOrCreateTag();
		tag.putString("Text", getText());
		letter.setTag(tag);
	}

	private void sendUpdate() {
		int slot = this.hand == InteractionHand.MAIN_HAND ? this.playerEntity.getInventory().selected : 40;
		Furnish.networkChannel.sendToServer(new ItemStackUpdateMessage(slot, letter));
	}

	@Override
	protected void init() {
		super.init();
		if(editable) {
			this.addRenderableWidget(new Button(this.width / 2 - 102, 196, 100, 20, SIGN_LETTER, (var) -> {
				save();
				Letter.signLetter(letter, playerEntity.getGameProfile().getName());
				sendUpdate();
				this.minecraft.setScreen(null);
			}));
			this.addRenderableWidget(new Button(this.width / 2 + 2, 196, 100, 20, CommonComponents.GUI_DONE, (var) -> {
				save();
				sendUpdate();
				this.minecraft.setScreen(null);
			}));
		} else {
			this.addRenderableWidget(new Button(this.width / 2 - 50, 196, 100, 20, CommonComponents.GUI_DONE, (var) -> {
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
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, LETTER_BACKGROUND);
		int startX = (this.width - 192) / 2;
		this.blit(ms, startX, 2, 0, 0, 192, 192);

		// text and cursor rendering
		if (editable) {
			if(frameTick / 6 % 2 == 0) {
				font.drawWordWrap(new TextComponent(letterText).append("_"), startX + 36, 20, 108, 0);
			} else {
				font.drawWordWrap(new TextComponent(letterText).append(" "), startX + 36, 20, 108, 0);
			}
		} else {
			font.drawWordWrap(new TextComponent(letterText), startX + 36, 20, 108, 0);
		}
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
