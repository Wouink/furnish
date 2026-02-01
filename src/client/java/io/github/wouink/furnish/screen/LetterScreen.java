package io.github.wouink.furnish.screen;

import io.github.wouink.furnish.Furnish;
import io.github.wouink.furnish.FurnishContents;
import io.github.wouink.furnish.item.Letter;
import io.github.wouink.furnish.network.UpdateLetterC2S;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class LetterScreen extends Screen {
    private static final Component SCREEN_NAME = Component.translatable("item.furnish.letter");
    private static final Component SIGN_LETTER = Component.translatable("book.signButton");
    private static final ResourceLocation LETTER_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Furnish.MOD_ID, "textures/gui/letter.png");

    // Max length is 16 lines of 18 characters
    private static final int LETTER_MAX_LENGTH = 288;

    private final Player playerEntity;
    private boolean editable = false;
    private String letterText;
    private TextFieldHelper letterEdit = null;
    private int frameTick = 0;
    private final int slot;

    public LetterScreen(ItemStack letter, Player playerEntity, int slot) {
        super(SCREEN_NAME);
        this.playerEntity = playerEntity;
        this.slot = slot;
        letterText = letter.has(FurnishContents.LETTER_TEXT) ? letter.get(FurnishContents.LETTER_TEXT) : "";
        editable = Letter.canEdit(letter);
        if(editable) {
            letterEdit = new TextFieldHelper(this::getText, this::setText, this::getClipboard, this::setClipboard, (s) -> s.length() < LETTER_MAX_LENGTH);
            Furnish.LOGGER.debug("Letter is editable");
        } else Furnish.LOGGER.debug("Letter is not editable");
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

    private void sendUpdate(Optional<String> author) {
        Furnish.LOGGER.debug("Sending letter update to server");
        ClientPlayNetworking.send(new UpdateLetterC2S(slot, getText(), author));
    }

    @Override
    protected void init() {
        super.init();
        if(editable) {
            this.addRenderableWidget(Button.builder(SIGN_LETTER, (button) -> {
                sendUpdate(Optional.of(playerEntity.getGameProfile().getName()));
                this.minecraft.setScreen(null);
            }).bounds(this.width / 2 - 102, 196, 100, 20).build());
            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                sendUpdate(Optional.empty());
                this.minecraft.setScreen(null);
            }).bounds(this.width / 2 + 2, 196, 100, 20).build());
        } else {
            this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (button) -> {
                this.minecraft.setScreen(null);
            }).bounds(this.width / 2 - 50, 196, 100, 20).build());
        }
    }

    @Override
    public boolean charTyped(char c, int n) {
        if(super.charTyped(c, n)) {
            Furnish.LOGGER.debug("super.charTyped true");
            return true;
        }
        if(editable && StringUtil.isAllowedChatCharacter(c)) {
            Furnish.LOGGER.debug("Character allowed: " + c);
            letterEdit.insertText(Character.toString(c));
            Furnish.LOGGER.debug("letterText now contains " + getText());
            return true;
        }
        Furnish.LOGGER.debug("Character not allowed: " + c);
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.setFocused(null);
        int startX = (this.width - 192) / 2;

        // text and cursor rendering
        if(editable) {
            if(frameTick / 6 % 2 == 0) {
                guiGraphics.drawWordWrap(font, Component.literal(letterText).append("_").setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
            } else {
                guiGraphics.drawWordWrap(font, Component.literal(letterText).append(" ").setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
            }
        } else {
            guiGraphics.drawWordWrap(font, Component.literal(letterText).setStyle(Style.EMPTY.withColor(ChatFormatting.BLACK)), startX + 36, 20, 108, 0);
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderTransparentBackground(guiGraphics);
        int startX = (this.width - 192) / 2;
        guiGraphics.blit(LETTER_BACKGROUND, startX, 2, 0, 0, 192, 192);
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
